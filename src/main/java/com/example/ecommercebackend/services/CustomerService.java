package com.example.ecommercebackend.services;

import com.example.ecommercebackend.auth.AuthenticationResponse;
import com.example.ecommercebackend.auth.PasswordUpdateRequest;
import com.example.ecommercebackend.customer.ChangePasswordRequest;
import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.dtos.CustomerDto;
import com.example.ecommercebackend.entities.TemporaryPassword;
import com.example.ecommercebackend.exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.mappers.CustomerMapper;
import com.example.ecommercebackend.repositories.CustomerRepository;
import com.example.ecommercebackend.repositories.TemporaryPasswordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository repository;
    private final TemporaryPasswordRepository tempRepository;
    private final EmailSenderService senderService;

    public CustomerDto getCustomerById(String name) {
        Customer customer = repository.findByEmail(name).orElseThrow(() -> new ResourceNotFoundException(
                "Customer with id was not found"
        ));
        customer.setPassword(null);
        return CustomerMapper.mapToCustomerDto(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return customers.stream().map(CustomerMapper::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    public boolean verifyCustomer(Integer id, String code) {
        Optional<Customer> customerOptional = repository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (customer.getVerificationCode().equals(code)) {
                customer.setActive(true);
                //customer.setLastLogIn(new Date());
                //String ipAddress = request.getRemoteAddr();
                //customer.setLastIpAddress(ipAddress);
                repository.save(customer);
                return true;
            }
        }
        return false;
    }

    public boolean resetPassword(String email) {
        try {
            Optional<Customer> customerOptional = repository.findByEmail(email);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                String tempPassword = generateTempPassword();
                String hashedTempPassword = passwordEncoder.encode(tempPassword);
                saveTemporaryPassword(hashedTempPassword, customer);
                repository.save(customer);
                sendResetPasswordEmail(email, tempPassword);
                return true;
            } else {
                System.out.println("Customer not found");
                return false;
            }
        } catch (IncorrectResultSizeDataAccessException ex) {
            System.out.println("Error: Multiple accounts found with the same email address.");
            return false;
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
            return false;
        }
    }

    private void saveTemporaryPassword(String passwd, Customer customer) {
        TemporaryPassword tempPasswd = TemporaryPassword.builder()
                .customer(customer)
                .token(passwd)
                .expired(false)
                .revoked(false)
                .build();
        tempRepository.save(tempPasswd);
    }

    public String generateTempPassword() {
        String characters = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random random = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            tempPassword.append(characters.charAt(index));
        }

        return tempPassword.toString();
    }

    public void sendResetPasswordEmail(String email, String tempPassword) {
        String subject = "Reset Your Password";
        String message = "Your temporary password is: " + tempPassword
                + "\nPlease use this password to log in and then change your password immediately.";

        senderService.sendEmail(email, subject, message);
    }

    public boolean updatePassword(String emailAddress, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match.");
        }

        Optional<Customer> customerOptional = repository.findByEmail(emailAddress);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            customer.setPassword(hashedPassword);
            repository.save(customer);
            return true;
        }
        return false;
    }

    /*public String generateTempPassword(String email) {
        Customer customer = new Customer();
        customer.setEmail(email);
        return jwtService.buildToken(new HashMap<>(), customer, 0);
    }*/

}
