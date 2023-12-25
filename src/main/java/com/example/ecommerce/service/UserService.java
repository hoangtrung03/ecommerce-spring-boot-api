package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String email);
    ResultWithPaginationResponse<List<UserDetailResponse>> getAllUser(int page, int size, String sortBy, String sortDirection);
    ResultResponse<UserDetailResponse> updateUser(User currentUser, UserDetailRequest u);
    ResultResponse<UserDetailResponse> updateUserById(Integer id, UserDetailRequest u);
    ResultResponse<String> deleteUserById(Integer id);
}
