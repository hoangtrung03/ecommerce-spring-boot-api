package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return user;
    }

    @Override
    public ResultWithPaginationResponse<List<UserDetailResponse>> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDetailResponse> userDetailResponses = userPage.getContent().stream()
                .map(UserDetailResponse::fromUser)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                userPage.getNumber(), userPage.getSize(), userPage.getTotalPages());

        return new ResultWithPaginationResponse<List<UserDetailResponse>>(
                StatusCode.SUCCESS,
                "Get all users success",
                userDetailResponses,
                paginationInfo
        );
    }

    @Override
    public ResultResponse<UserDetailResponse> updateUser(User currentUser, UserDetailRequest u) {
        if(currentUser == null){
            return new ResultResponse<>(StatusCode.NOT_FOUND, "User not found", null);
        }

        User user = userRepository.findByEmail(currentUser.getEmail());
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        userRepository.save(user);

        return new ResultResponse<>(StatusCode.SUCCESS, "Update user success", UserDetailResponse.fromUser(user));
    }
}
