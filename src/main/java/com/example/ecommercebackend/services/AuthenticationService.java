package com.example.ecommercebackend.services;

import com.example.ecommercebackend.auth.AuthenticationRequest;
import com.example.ecommercebackend.auth.AuthenticationResponse;
import com.example.ecommercebackend.auth.PasswordUpdateRequest;
import com.example.ecommercebackend.auth.RegisterRequest;
import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.customer.Role;
import com.example.ecommercebackend.entities.TemporaryPassword;
import com.example.ecommercebackend.repositories.CustomerRepository;
import com.example.ecommercebackend.repositories.TemporaryPasswordRepository;
import com.example.ecommercebackend.repositories.TokenRepository;
import com.example.ecommercebackend.token.Token;
import com.example.ecommercebackend.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomerRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailService;
    private final CustomerService customerService;
    private final TemporaryPasswordRepository tempRepository;

    public ResponseEntity<?> register(RegisterRequest request) {
        Optional<Customer> existingCustomer = repository.findByEmail(request.getEmailAddress());
        if (existingCustomer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with that email already exists.");
        }
        var customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmailAddress())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .active(false)
                .build();
        String verificationCode = jwtService.buildToken(new HashMap<>(), customer, -1);
        customer.setVerificationCode(verificationCode);
        var savedCustomer = repository.save(customer);
        //var jwtToken = jwtService.generateToken(customer);
        //var refreshToken = jwtService.generateRefreshToken(customer);
        //saveCustomerToken(savedCustomer, jwtToken);
        sendVerificationMail(request.getEmailAddress(), verificationCode, customer.getId());
        return ResponseEntity.ok("Account created. Check e-mail");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailAddress(),
                        request.getPassword()
                )
        );
        var customer = repository.findByEmail(request.getEmailAddress()).orElseThrow();
        var jwtToken = jwtService.generateToken(customer);
        var refreshToken = jwtService.generateRefreshToken(customer);
        revokeAllCustomerTokens(customer);
        saveCustomerToken(customer, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveCustomerToken(Customer customer, String jwtToken) {
        var token = Token.builder()
                .customer(customer)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllCustomerTokens(Customer customer) {
        var validTokens = tokenRepository.findAllValidTokenByCustomer(customer.getId());
        if(validTokens.isEmpty()) return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return;
        refreshToken = authHeader.substring(7);
        email = jwtService.extractUsername(refreshToken);
        if(email != null) {
            var customer = this.repository.findByEmail(email).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, customer)) {
                var accessToken = jwtService.generateToken(customer);
                revokeAllCustomerTokens(customer);
                saveCustomerToken(customer, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void sendVerificationMail(String email, String code, Integer id) {
        emailService.sendEmail(email, "E-commerce Registration Code",
                "Your registration activation link: " +
                "http://localhost:8080/api/auth/verify?" +
                "code=" + code + "&id=" + id);
    }

    public boolean verifyCustomer(Integer id, String code) {
        return customerService.verifyCustomer(id, code);
    }

    public boolean verifyTempPassword(String emailAddress, String tempPassword) {
        Optional<Customer> customerOptional = repository.findByEmail(emailAddress);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            TemporaryPassword passwd = tempRepository.findValidTokenByCustomer(customer.getId());
            if (passwd != null && passwordEncoder.matches(tempPassword, passwd.token)) {
                passwd.revoked = true;
                tempRepository.save(passwd);
                return true;
            }
        }
        return false;
    }

    public boolean validChange(String email, String token) {
        Optional<Customer> customerOptional = repository.findByEmail(email);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            TemporaryPassword passwd = tempRepository.findAgainValid(customer.getId());
            passwd.used = true;
            tempRepository.save(passwd);
            return passwordEncoder.matches(token, passwd.token);
        }
        return false;
    }
}
