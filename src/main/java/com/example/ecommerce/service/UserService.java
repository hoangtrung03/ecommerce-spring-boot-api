package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String email);
    ResultResponse<List<UserDetailResponse>> getAllUser();
    ResultResponse<UserDetailResponse> updateUser(User currentUser, UserDetailRequest u);
}
