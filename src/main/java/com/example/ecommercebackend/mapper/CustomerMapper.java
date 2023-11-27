package com.example.ecommercebackend.mapper;

import com.example.ecommercebackend.dto.CustomerDto;
import com.example.ecommercebackend.entity.Customer;

public class CustomerMapper {
    public static CustomerDto mapToCustomerDto(Customer customer){
        return new CustomerDto(
            customer.getId(),
            customer.getUserName(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmailAddress(),
            customer.getPhoneNumber(),
            customer.getPassword(),
            customer.getSalt(),
            customer.getDateOfBirth(),
            customer.getLastIpAddress(),
            customer.getLastLogIn()
        );
    }

    public static Customer mapToCustomer(CustomerDto customerDto){
        return new Customer(
            customerDto.getId(),
            customerDto.getUserName(),
            customerDto.getFirstName(),
            customerDto.getLastName(),
            customerDto.getEmailAddress(),
            customerDto.getPhoneNumber(),
            customerDto.getPassword(),
            customerDto.getSalt(),
            customerDto.getDateOfBirth(),
            customerDto.getLastIpAddress(),
            customerDto.getLastLogIn()
        );
    }
}
