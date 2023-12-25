package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Date date_of_birth;
    private String avatar;
    private String address;
    private String phone;
    private String gender;
    private String country;
    private String city;
    private String bio;
    private Set<RoleResponse> roles;

    public static UserDetailResponse fromUser(User user) {
        Set<RoleResponse> roleDTOs = user.getRole()
                .stream()
                .map(role -> new RoleResponse(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return new UserDetailResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getDate_of_birth(),
                user.getAvatar(),
                user.getAddress(),
                user.getPhone(),
                user.getGender(),
                user.getCountry(),
                user.getCity(),
                user.getBio(),
                roleDTOs
        );
    }
}
