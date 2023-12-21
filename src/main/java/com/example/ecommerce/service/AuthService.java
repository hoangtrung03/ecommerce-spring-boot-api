package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.AuthRequest;
import com.example.ecommerce.dto.request.RegisterRequest;
import com.example.ecommerce.dto.response.AuthResponse;
import com.example.ecommerce.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse authenticate(AuthRequest request);

    void revokeAllUserTokens(User user);
    AuthResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
