package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    @GetMapping("/me")
    public ResponseEntity<ResultResponse<UserDetailResponse>> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            ResultResponse<UserDetailResponse> response = new ResultResponse<>(StatusCode.NOT_FOUND, "User not found", null);

            return ResponseEntity.ok(response);
        }

        UserDetailResponse userDTO = UserDetailResponse.fromUser(currentUser);
        ResultResponse<UserDetailResponse> response = new ResultResponse<>(StatusCode.SUCCESS, "Get user profile success", userDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ResultResponse<List<UserDetailResponse>>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/update")
    public ResponseEntity<ResultResponse<UserDetailResponse>> updateUser(@AuthenticationPrincipal User currentUser, @RequestBody UserDetailRequest u){
        return ResponseEntity.ok(userService.updateUser(currentUser, u));
    }
}
