package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.AuthRequest;
import com.example.ecommerce.dto.request.EmailVerifyRequest;
import com.example.ecommerce.dto.request.RegisterRequest;
import com.example.ecommerce.dto.response.AuthResponse;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.Token;
import com.example.ecommerce.entity.TokenType;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.UserVerifyStatus;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.TokenRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.JwtService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailServiceImpl emailService;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Override
    public AuthResponse register(RegisterRequest request) {
        var isExistUser = repository.findByEmail(request.getEmail());

        if (isExistUser != null) {
            return AuthResponse.builder().message(Messages.USER_ALREADY_EXISTS).build();
        }

        Role roleUser = roleRepository.findByName("USER");

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userVerifyStatus(UserVerifyStatus.Unverified)
                .role(new HashSet<>(Collections.singletonList(roleUser)))
                .build();
        // Generate email verification token
        var emailVerifyToken = jwtService.generateEmailVerifyToken(user);
        user.setVerificationCode(emailVerifyToken);
        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        // Generate refresh token
        var refreshToken = jwtService.generateRefreshToken(user);

        var savedUser = repository.save(user);
        saveUserToken(savedUser, refreshToken);

        try {
            sendVerificationEmail(savedUser, "http://localhost:3000");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return AuthResponse.builder()
                .message(Messages.USER_REGISTER_SUCCESS)
                .data(AuthResponse.AuthTokens.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .expiresAccessTokenIn(jwtService.getExtractExpirationToken(jwtToken))
                        .expiresRefreshTokenIn(jwtService.getExtractExpirationToken(refreshToken))
                        .build())
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail());

        if (user == null) {
            return AuthResponse.builder().message(Messages.USER_NOT_FOUND).build();
        } else {
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, refreshToken);

            return AuthResponse.builder()
                    .message(Messages.LOGIN_SUCCESS)
                    .data(AuthResponse.AuthTokens.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .expiresAccessTokenIn(jwtService.getExtractExpirationToken(jwtToken))
                            .expiresRefreshTokenIn(jwtService.getExtractExpirationToken(refreshToken))
                            .build())
                    .build();
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token existingToken = tokenRepository.findByUser(user);

        if (existingToken != null) {
            existingToken.setToken(jwtToken);
            existingToken.setExpired(false);
            existingToken.setRevoked(false);
            tokenRepository.save(existingToken);
        } else {
            Token newToken = Token.builder()
                    .user(user)
                    .token(jwtToken)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenRepository.save(newToken);
        }
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public AuthResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return AuthResponse.builder().message(Messages.INVALID_ACCESS_TOKEN).build();
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail);

            if (user == null) {
                return AuthResponse.builder().message(Messages.USER_NOT_FOUND).data("").build();
            }

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, refreshToken);

                return AuthResponse.builder()
                        .message(Messages.REFRESH_TOKEN_SUCCESS)
                        .data(AuthResponse.AuthTokens.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .expiresAccessTokenIn(jwtService.getExtractExpirationToken(accessToken))
                                .expiresRefreshTokenIn(jwtService.getExtractExpirationToken(refreshToken))
                                .build())
                        .build();
            }
        }

        return AuthResponse.builder().message(Messages.REFRESH_TOKEN_FAILED).build();
    }

    @Override
    public AuthResponse verifyEmail(EmailVerifyRequest token) {
        String verifyToken = token.getEmail_verify_token();
        String userEmail;

        if (verifyToken == null) {
            return AuthResponse.builder().message(Messages.INVALID_ACCESS_TOKEN).build();
        }

        userEmail = jwtService.extractUsername(verifyToken);

        if (userEmail == null) {
            return AuthResponse.builder().message(Messages.INVALID_TOKEN).data("").build();
        }

        User user = repository.findByEmail(userEmail);

        if (user == null) {
            return AuthResponse.builder().message(Messages.USER_NOT_FOUND).data("").build();
        }

        if (user.getVerificationCode().equals(verifyToken)) {
            user.setUserVerifyStatus(UserVerifyStatus.Verified);
            repository.save(user);
            return AuthResponse.builder().message(Messages.EMAIL_VERIFIED).data(userEmail).build();
        }

        return AuthResponse.builder().message(Messages.INVALID_TOKEN).data("").build();
    }

    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        int idEmail = (int) (Math.random() * 35421) + user.getId();
        String toAddress = user.getEmail();
        String subject = "Please verify your registration " + "#" + idEmail;
        String content = "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\" style=\"text-decoration: none; font-size: 18px; color: #E60000;\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";
        String verifyURL = siteURL + "/verify?token=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        emailService.sendVerificationEmail("vht03032000@gmail.com", "Ecommerce", toAddress, subject, content);
    }
}
