package com.example.ecommercebackend.dtos;

import com.example.ecommercebackend.customer.Role;
import com.example.ecommercebackend.token.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Date dateOfBirth;
    private boolean active;
    /*private String lastIpAddress;
    private Date lastLogIn;*/
    private Role role;
}