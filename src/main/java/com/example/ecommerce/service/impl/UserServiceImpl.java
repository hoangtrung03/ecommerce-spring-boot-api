package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.ForgotPasswordRequest;
import com.example.ecommerce.dto.request.PasswordRequest;
import com.example.ecommerce.dto.request.UserDetailRequest;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.UserDetailResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.TokenRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.PasswordUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        return user;
    }

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<UserDetailResponse>>> getAllUser(int page, int size, String sortBy, String sortDirection) {
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_USERS_SUCCESS, userDetailResponses, paginationInfo));
    }

    @Override
    public ResponseEntity<ResultResponse<UserDetailResponse>> updateUser(User currentUser, UserDetailRequest u) {
        if (currentUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND, null));
        }

        User user = userRepository.findByEmail(currentUser.getEmail());
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        user.setDate_of_birth(u.getDate_of_birth());
        user.setAddress(u.getAddress());
        user.setPhone(u.getPhone());
        user.setGender(u.getGender());
        user.setCountry(u.getCountry());
        user.setCity(u.getCity());
        user.setBio(u.getBio());
        user.setAvatar(u.getAvatar());
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_USER_SUCCESS, UserDetailResponse.fromUser(user)));
    }

    @Override
    public ResponseEntity<ResultResponse<UserDetailResponse>> updateUserById(Integer id, UserDetailRequest u) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND, null));
        }

        User userEntity = user.get();
        userEntity.setFirstname(u.getFirstname());
        userEntity.setLastname(u.getLastname());
        userEntity.setDate_of_birth(u.getDate_of_birth());
        userEntity.setAddress(u.getAddress());
        userEntity.setPhone(u.getPhone());
        userEntity.setGender(u.getGender());
        userEntity.setCountry(u.getCountry());
        userEntity.setCity(u.getCity());
        userEntity.setBio(u.getBio());
        userEntity.setAvatar(u.getAvatar());
        userRepository.save(userEntity);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_USER_SUCCESS, UserDetailResponse.fromUser(userEntity)));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> deleteUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND, null));
        }

        tokenRepository.deleteTokensByUserId(id);
        userRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_USER_SUCCESS, null));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> deleteByMultiIds(List<Integer> ids) {
        for (Integer id : ids) {
            tokenRepository.deleteTokensByUserId(id);
            userRepository.deleteById(id);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_USER_SUCCESS, null));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> changePassword(User currentUser, PasswordRequest user) {
        if (currentUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND, null));
        }

        User userEntity = userRepository.findByEmail(currentUser.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.CHANGE_PASSWORD_SUCCESS, currentUser.getEmail()));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> forgotPassword(ForgotPasswordRequest email) {
        User user = userRepository.findByEmail(email.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND, null));
        }

        int idEmail = (int) (Math.random() * 35421) + user.getId();
        String toAddress = user.getEmail();
        String subject = "Forgot password " + " - " + idEmail;
        String password = PasswordUtils.generatePassword();
        String content =
                "A new password has been generated:<br>"
                        + "Password: " + password + "<br>"
                        + "Thank you,<br>"
                        + "Your company name.";

        try {
            emailService.sendVerificationEmail("vht03032000@gmail.com", "Ecommerce", toAddress, subject, content);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.FORGOT_PASSWORD_SUCCESS, null));
    }

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<UserDetailResponse>>> searchUser(String keyword, Integer page, Integer size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageAble = PageRequest.of(page - 1, size, direction, sortBy);
        Page<User> users = userRepository.search(keyword.toLowerCase(), pageAble);

        if (users.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultWithPaginationResponse<>(
                            StatusCode.NOT_FOUND,
                            Messages.USER_NOT_FOUND,
                            null,
                            new PaginationInfo()
                    ));
        }

        List<UserDetailResponse> userDetailResponses = users.getContent().stream()
                .map(UserDetailResponse::fromUser)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                users.getNumber(), users.getSize(), users.getTotalPages());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_USERS_SUCCESS, userDetailResponses, paginationInfo));
    }

}
