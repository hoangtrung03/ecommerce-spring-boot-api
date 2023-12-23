package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
                roleDTOs
        );
    }
}
