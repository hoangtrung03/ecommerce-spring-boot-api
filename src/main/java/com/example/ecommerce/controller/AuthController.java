package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.AuthRequest;
import com.example.ecommerce.dto.request.EmailVerifyRequest;
import com.example.ecommerce.dto.request.RefreshTokenRequest;
import com.example.ecommerce.dto.request.RegisterRequest;
import com.example.ecommerce.dto.response.AuthResponse;
import com.example.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid AuthRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody @Valid RefreshTokenRequest refreshRequest
    ) {
        return service.refreshToken(refreshRequest);
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(
            @RequestBody EmailVerifyRequest request
    ) {
        return ResponseEntity.ok(service.verifyEmail(request));
    }
}
