package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.TokenRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return user;
    }

    @Override
    public ResultWithPaginationResponse<List<UserDetailResponse>> getAllUser(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDetailResponse> userDetailResponses = userPage.getContent().stream()
                .map(UserDetailResponse::fromUser)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                userPage.getNumber(), userPage.getSize(), userPage.getTotalPages());

        return new ResultWithPaginationResponse<>(
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

    @Override
    public ResultResponse<UserDetailResponse> updateUserById(Integer id, UserDetailRequest u) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            return new ResultResponse<>(StatusCode.NOT_FOUND, "User not found", null);
        }

        User userEntity = user.get();
        userEntity.setFirstname(u.getFirstname());
        userEntity.setLastname(u.getLastname());
        userRepository.save(userEntity);

        return new ResultResponse<>(StatusCode.SUCCESS, "Update user success", UserDetailResponse.fromUser(userEntity));
    }

    @Override
    public ResultResponse<String> deleteUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            return new ResultResponse<>(StatusCode.NOT_FOUND, "User not found", null);
        }

        tokenRepository.deleteTokensByUserId(id);
        userRepository.deleteById(id);
        return new ResultResponse<>(StatusCode.SUCCESS, "Delete user success", null);
    }
}
