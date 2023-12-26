package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.PasswordRequest;
import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserDetails loadUserByUsername(String email);
    ResponseEntity<ResultWithPaginationResponse<List<UserDetailResponse>>> getAllUser(int page, int size, String sortBy, String sortDirection);
    ResponseEntity<ResultResponse<UserDetailResponse>> updateUser(User currentUser, UserDetailRequest u);
    ResponseEntity<ResultResponse<UserDetailResponse>> updateUserById(Integer id, UserDetailRequest u);
    ResponseEntity<ResultResponse<String>> deleteUserById(Integer id);
    ResponseEntity<ResultResponse<String>> deleteByMultiIds(List<Integer> ids);
    ResponseEntity<ResultResponse<String>> changePassword(User currentUser, PasswordRequest user);
}
