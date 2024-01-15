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
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final EmailSenderService senderService;
    private final CustomerService customerService;

    @PostMapping("/checkout")
    public ResponseEntity<String> saveCartAndCheckout(@RequestBody CartDto cartDto, Principal principal) {
        try {
            String emailToUse = (principal != null) ? principal.getName() : cartDto.getEmail();
            cartDto.setEmail(emailToUse);

            if (principal != null) {
                // Authenticated user
                String email = principal.getName();
                CustomerDto customer = customerService.getCustomerByEmail(email);
                if (customer != null && customer.getId() != null) {
                    cartDto.setCustomerId(customer.getId());
                } else {
                    return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
                }
            } else if (cartDto.getEmail() == null || cartDto.getEmail().isEmpty()) {
                return new ResponseEntity<>("Email is required for non-authenticated users", HttpStatus.BAD_REQUEST);
            }

            // Create cart for both authenticated and non-authenticated users
            CartDto createdCart = cartService.createCart(cartDto);
            senderService.sendEmail(cartDto.getEmail() != null ? cartDto.getEmail() : principal.getName(), "Purchase Confirmation", "Thank you for your purchase!");
            return new ResponseEntity<>("Cart saved and email sent.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to save the cart: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
