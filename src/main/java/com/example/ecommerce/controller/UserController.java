package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ResultResponse<List<UserDetailResponse>>> getAllUser(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_direction", required = false) String sortDirection
    ) {
        return ResponseEntity.ok(userService.getAllUser(page, size, sortBy, sortDirection));
    }

    @PutMapping("/update")
    public ResponseEntity<ResultResponse<UserDetailResponse>> updateUser(@Valid @AuthenticationPrincipal User currentUser, @RequestBody UserDetailRequest u){
        return ResponseEntity.ok(userService.updateUser(currentUser, u));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ResultResponse<UserDetailResponse>> updateUser(
            @Valid
            @PathVariable("id") Integer id,
            @RequestBody UserDetailRequest u
    ){
        return ResponseEntity.ok(userService.updateUserById(id, u));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResultResponse<String>> deleteUser(
            @PathVariable("id") Integer id
    ) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<String>> deleteMultiUsers(
            @RequestParam("ids") List<Integer> ids
    ) {
        return ResponseEntity.ok(userService.deleteByMultiIds(ids));
    }
}
