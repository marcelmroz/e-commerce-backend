package com.example.ecommercebackend.mappers;

import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.dtos.CustomerDto;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer){
        return new CustomerDto(
                customer.getId(),
                customer.getUsername(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getPassword(),
                customer.getDateOfBirth(),
                /*customer.getLastIpAddress(),
                customer.getLastLogIn(),*/
                customer.isEnabled(),
                customer.getRole()
        );
    }

    public static Customer mapToCustomer(CustomerDto customerDto){
        return new Customer(
                customerDto.getId(),
                customerDto.getUsername(),
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getEmail(),
                customerDto.getPhoneNumber(),
                customerDto.getPassword(),
                customerDto.getDateOfBirth(),
                /*customerDto.getLastIpAddress(),
                customerDto.getLastLogIn(),*/
                customerDto.isActive(),
                null,
                customerDto.getRole(),
                null,
                null
        );
    }

}
