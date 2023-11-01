package com.example.ecommercebackend.service;

import com.example.ecommercebackend.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto getCustomerById(Long customerId);

    List<CustomerDto> getAllCustomers();

    CustomerDto updateCustomer(Long customerId, CustomerDto updatedCustomer);

    void deleteCustomer(Long customerId);
}
