package com.example.ecommerce.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    );

    String generateRefreshToken(
            UserDetails userDetails
    );

    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);

    String generateEmailVerifyToken(UserDetails userDetails);

    long getExtractExpirationToken(String token);

    boolean validateToken(String token);

    String getToken (HttpServletRequest httpServletRequest);
}
