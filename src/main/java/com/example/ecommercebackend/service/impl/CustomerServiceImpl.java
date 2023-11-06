package com.example.ecommercebackend.service.impl;

import com.example.ecommercebackend.dto.CustomerDto;
import com.example.ecommercebackend.entity.Customer;
import com.example.ecommercebackend.exception.ResourceNotFoundException;
import com.example.ecommercebackend.mapper.CustomerMapper;
import com.example.ecommercebackend.repository.CustomerRepository;
import com.example.ecommercebackend.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with given id: " + customerId + "does not exist."));
        return CustomerMapper.mapToCustomerDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerMapper::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto updatedCustomer) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Product with given id: " + customerId + "does not exist.")
        );
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setEmailAddress(updatedCustomer.getEmailAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customer.setPassword(updatedCustomer.getPassword());
        customer.setDateOfBirth(updatedCustomer.getDateOfBirth());

        Customer updatedCustomerObj = customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(updatedCustomerObj);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Product with given id: " + customerId + "does not exist.")
        );

        customerRepository.deleteById(customerId);
    }
}
