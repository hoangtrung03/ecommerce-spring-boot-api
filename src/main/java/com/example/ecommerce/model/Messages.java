package com.example.ecommerce.model;

import lombok.Data;

@Data
public class Messages {
    //  User messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String UPDATE_USER_SUCCESS = "Update user success";
    public static final String DELETE_USER_SUCCESS = "Delete user success";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String GET_ALL_USERS_SUCCESS = "Get all users success";
    public static final String USER_REGISTER_SUCCESS = "User registered successfully";
    public static final String SEARCH_SUCCESS = "Search success";
    public static final String LOGIN_SUCCESS = "Login successfully";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_ACCESS_TOKEN = "Invalid access token";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh token successfully";
    public static final String REFRESH_TOKEN_FAILED = "Refresh token failed";
    public static final String CHANGE_PASSWORD_SUCCESS = "Change password success";
    public static final String FORGOT_PASSWORD_SUCCESS = "Forgot password success";

    // Email messages
    public static final String GET_EMAIL_SUCCESS = "Get emails success";
    public static final String EMAIL_VERIFIED = "Email verified successfully";
    public static final String EMAIL_TYPE_ALREADY_EXISTS = "Email type already exists";
    public static final String ADD_EMAIL_SUCCESS = "Add email success";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String UPDATE_EMAIL_SUCCESS = "Update email success";
    public static final String DELETE_EMAIL_SUCCESS = "Delete email success";

    // Role messages
    public static final String GET_ALL_ROLE_SUCCESS = "Get all roles success";
    public static final String GET_ROLE_SUCCESS = "Get role success";
    public static final String ROLE_CREATED = "Role created successfully";
    public static final String ROlE_NOT_FOUND = "Role name cannot be null or empty";
    public static final String ROLE_ASSIGNED = "Role assigned to user successfully";
    public static final String ROLE_REVOKED = "Role revoked from user successfully";
    public static final String ROLE_DELETED = "Role deleted successfully";
    public static final String ROLE_NOT_DELETABLE = "Role is not deletable";
    public static final String ROLE_UPDATED = "Role updated successfully";
    public static final String ROLE_NOT_UPDATABLE = "Role is not updatable";
    public static final String ROLE_ALREADY_EXISTS = "Role already exists";

    // Product messages
    public static final String GET_ALL_PRODUCT_SUCCESS = "Get all products success";
    public static final String GET_PRODUCT_SUCCESS = "Get product success";
    public static final String ADD_PRODUCT_SUCCESS = "Add product success";
    public static final String UPDATE_PRODUCT_SUCCESS = "Update product success";
    public static final String DELETE_PRODUCT_SUCCESS = "Delete product success";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_EXIST = "Product exist";
    public static final String INVALID_PRODUCT_TYPE = "Invalid product type";

    // Category messages
    public static final String GET_ALL_CATEGORY_SUCCESS = "Get all categories success";
    public static final String GET_CATEGORY_SUCCESS = "Get category success";
    public static final String ADD_CATEGORY_SUCCESS = "Add category success";
    public static final String DELETE_CATEGORY_SUCCESS = "Delete category success";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String UPDATE_CATEGORY_SUCCESS = "Update category success";
    public static final String PARENT_CATEGORY_NOT_FOUND = "Parent category not found";
    public static final String SEARCH_CATEGORY_SUCCESS = "Search category success";
    public static final String CATEGORY_EXIST = "Category exist";

    // Variant messages
    public static final String VARIANT_NOT_FOUND = "Variant not found";
    public static final String VARIANT_ATTRIBUTE_NOT_FOUND = "Variant attribute not found";
    public static final String VARIANT_VALUE_NOT_FOUND = "Variant value not found";
}
