package com.example.ecommercebackend.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    private String currentPasswd;
    private String newPasswd;
    private String confirmPasswd;

}
