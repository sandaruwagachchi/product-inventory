package com.example.product_inventory.controller;

import com.example.product_inventory.dto.LoginRequest;
import com.example.product_inventory.dto.LoginResponse;
import com.example.product_inventory.dto.RegisterRequest;
import com.example.product_inventory.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        LoginResponse response = authenticationService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

