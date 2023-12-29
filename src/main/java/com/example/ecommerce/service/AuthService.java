package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.AuthRequest;
import com.example.ecommerce.dto.request.EmailVerifyRequest;
import com.example.ecommerce.dto.request.RefreshTokenRequest;
import com.example.ecommerce.dto.request.RegisterRequest;
import com.example.ecommerce.dto.response.AuthResponse;
import com.example.ecommerce.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(AuthRequest request);

    void revokeAllUserTokens(User user);

    ResponseEntity<AuthResponse> refreshToken(
            RefreshTokenRequest refreshRequest
    );

    AuthResponse verifyEmail(EmailVerifyRequest token);
}
