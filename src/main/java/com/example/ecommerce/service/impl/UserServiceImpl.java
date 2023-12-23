package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public ResultResponse<List<UserDetailResponse>> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDetailResponse> userDetailResponses = users.stream()
                .map(UserDetailResponse::fromUser)
                .collect(Collectors.toList());

        return new ResultResponse<>(StatusCode.SUCCESS, "Get all user success", userDetailResponses);
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
