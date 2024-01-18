package com.example.ecommercebackend.controllers;


import com.example.ecommercebackend.dtos.CartDto;
import com.example.ecommercebackend.dtos.CustomerDto;
import com.example.ecommercebackend.services.CartService;
import com.example.ecommercebackend.services.CustomerService;
import com.example.ecommercebackend.services.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final EmailSenderService senderService;
    private final CustomerService customerService;

    @PostMapping("/checkout")
    public ResponseEntity<String> saveCartAndCheckout(@RequestBody CartDto cartDto, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        String email = principal.getName();
        CustomerDto customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }

        if (customer.getId() == null) {
            return new ResponseEntity<>("Customer ID is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        cartDto.setCustomerId(customer.getId());
        try {
            CartDto createdCart = cartService.createCart(cartDto);
            String emailContent = createEmailContent(createdCart);
            senderService.sendEmail(customer.getEmail(), "Purchase Confirmation", emailContent);
            return new ResponseEntity<>("Cart saved and email sent.", HttpStatus.CREATED);

        } catch (Exception e) {
            // Log the exception for further investigation
            e.printStackTrace();
            return new ResponseEntity<>("Failed to save the cart: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String createEmailContent(CartDto cartDto) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Thank you for your purchase! Here are the details of your cart:\n\n");

        emailContent.append("\nTotal Price: ").append(cartDto.getTotalPrice());

        return emailContent.toString();
    }

}
