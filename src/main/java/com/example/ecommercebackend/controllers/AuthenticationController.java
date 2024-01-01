package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.auth.AuthenticationRequest;
import com.example.ecommercebackend.auth.AuthenticationResponse;
import com.example.ecommercebackend.auth.PasswordUpdateRequest;
import com.example.ecommercebackend.auth.RegisterRequest;
import com.example.ecommercebackend.entities.TemporaryPassword;
import com.example.ecommercebackend.services.AuthenticationService;
import com.example.ecommercebackend.services.CustomerService;
import com.example.ecommercebackend.services.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam Integer id, @RequestParam String code) {
        if(service.verifyCustomer(id, code)) {
            return ResponseEntity.ok("Account verified successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthenticationRequest request) {
        boolean resetStatus = customerService.resetPassword(request.getEmailAddress());
        if (resetStatus) {
            return ResponseEntity.ok("A temporary password has been sent to your email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password.");
        }
    }

    @PostMapping("/verify-temp-password")
    public ResponseEntity<?> verifyTempPassword(@RequestBody AuthenticationRequest request) {
        boolean response =  service.verifyTempPassword(request.getEmailAddress(), request.getPassword());
        if(response) {
            return ResponseEntity.ok("Password correct");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        if(request.getNewPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input format");
        }
        if(service.validChange(request.getEmailAddress(), request.getToken())){
            try {
                boolean updateStatus = customerService.updatePassword(
                        request.getEmailAddress(),
                        request.getNewPassword(),
                        request.getConfirmPassword()
                        );
                if (updateStatus) {
                    return ResponseEntity.ok("Password updated successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update password.");
                }
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }


}
