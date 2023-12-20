package com.example.ecommercebackend.service;

import com.example.ecommercebackend.auth.AuthenticationRequest;
import com.example.ecommercebackend.auth.AuthenticationResponse;

public interface AuthenticationService {

    public AuthenticationResponse authenticate(AuthenticationRequest request);


}
