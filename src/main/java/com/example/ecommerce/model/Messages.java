package com.example.ecommerce.model;

import lombok.Data;

@Data
public class Messages {
    //  User messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_REGISTER_SUCCESS = "User registered successfully";
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_ACCESS_TOKEN = "Invalid access token";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh token successfully";
    public static final String REFRESH_TOKEN_FAILED = "Refresh token failed";
    public static final String GET_ALL_USERS_SUCCESS = "Get all users success";
    public static final String CHANGE_PASSWORD_SUCCESS = "Change password success";
    public static final String FORGOT_PASSWORD_SUCCESS = "Forgot password success";

    // Email messages
    public static final String EMAIL_VERIFIED = "Email verified successfully";
    public static final String EMAIL_TYPE_ALREADY_EXISTS = "Email type already exists";
    public static final String ADD_EMAIL_SUCCESS = "Add email success";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String UPDATE_EMAIL_SUCCESS = "Update email success";
    public static final String DELETE_EMAIL_SUCCESS = "Delete email success";

    // Role messages
    public static final String ROLE_CREATED = "Role created successfully";
    public static final String ROlE_NOT_FOUND = "Role name cannot be null or empty";
    public static final String ROLE_ASSIGNED = "Role assigned to user successfully";
    public static final String ROLE_REVOKED = "Role revoked from user successfully";
    public static final String ROLE_DELETED = "Role deleted successfully";
    public static final String ROLE_NOT_DELETABLE = "Role is not deletable";
    public static final String ROLE_UPDATED = "Role updated successfully";
    public static final String ROLE_NOT_UPDATABLE = "Role is not updatable";
    public static final String ROLE_ALREADY_EXISTS = "Role already exists";
}
