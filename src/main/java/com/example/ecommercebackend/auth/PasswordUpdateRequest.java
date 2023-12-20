package com.example.ecommercebackend.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {
    private String emailAddress;
    private String newPassword;
    private String confirmPassword;
    private String token;
}
