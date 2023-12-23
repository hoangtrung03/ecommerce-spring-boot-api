package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<ResultResponse> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            ResultResponse response = new ResultResponse(StatusCode.NOT_FOUND, "User not found", null);

            return ResponseEntity.ok(response);
        }

        UserDetailResponse userDTO = UserDetailResponse.fromUser(currentUser);
        ResultResponse response = new ResultResponse(StatusCode.SUCCESS, "Get user profile success", userDTO);

        return ResponseEntity.ok(response);
    }
}
