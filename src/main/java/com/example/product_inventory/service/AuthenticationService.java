package com.example.product_inventory.service;

import com.example.product_inventory.dto.LoginRequest;
import com.example.product_inventory.dto.LoginResponse;
import com.example.product_inventory.dto.RegisterRequest;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(RegisterRequest registerRequest);
}

