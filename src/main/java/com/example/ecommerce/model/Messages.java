package com.example.ecommerce.model;

import lombok.Data;

@Data
public class Messages {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_REGISTER_SUCCESS = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String EMAIL_VERIFIED = "Email verified successfully";
    public static final String INVALID_ACCESS_TOKEN = "Invalid access token";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh token successfully";
    public static final String REFRESH_TOKEN_FAILED = "Refresh token failed";
    public static final String ROLE_CREATED = "Role created successfully";
    public static final String ROlE_NOT_FOUND = "Role name cannot be null or empty";
    public static final String ROLE_ASSIGNED = "Role assigned to user successfully";
    public static final String ROLE_REVOKED = "Role revoked from user successfully";
}
