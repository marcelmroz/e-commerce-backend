package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.auth.PasswordUpdateRequest;
import com.example.ecommercebackend.customer.ChangePasswordRequest;
import com.example.ecommercebackend.dtos.CustomerDto;
import com.example.ecommercebackend.exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/account")
    public ResponseEntity<?> getCustomerById(Principal principal) {
        try {
            CustomerDto customerDto = service.getCustomerById(principal.getName());
            return ResponseEntity.ok(customerDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }



}
