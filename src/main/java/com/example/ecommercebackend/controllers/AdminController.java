package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.dtos.CustomerDto;
import com.example.ecommercebackend.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CustomerService service;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomersForAdmin(){
        List<CustomerDto> customers = service.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

}
