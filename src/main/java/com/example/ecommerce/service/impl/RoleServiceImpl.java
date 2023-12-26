package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new role based on the provided role name.
     *
     * @param roleName the name of the role to be created
     * @return the newly created role
     */
    @Override
    public ResponseEntity<ResultResponse<Object>> createRole(RoleRequest roleName) {
        if (roleName == null || roleName.getName() == null || roleName.getName().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<Object>(StatusCode.BAD_REQUEST, Messages.ROlE_NOT_FOUND));
        }

        Role role = new Role();
        role.setName(roleName.getName());
        roleRepository.save(role);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<Object>(StatusCode.SUCCESS, Messages.ROLE_CREATED, roleName.getName()));
    }

    @Override
    public ResponseEntity<ResultResponse<Object>> assignRole(RoleRequest roleDTO, Integer userId) {
        Role role = roleRepository.findByName(roleDTO.getName());
        Optional<User> user = userRepository.findById(userId);

        if (role == null || role.getName().equals("USER")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.ROlE_NOT_FOUND)
            );
        }

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND)
            );
        }

        Set<Role> userRoles = user.get().getRole();
        userRoles.add(role);
        user.get().setRole(userRoles);
        userRepository.save(user.get());

        return ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ROLE_ASSIGNED));
    }

    @Override
    public ResponseEntity<ResultResponse<Object>> revokeRole(RoleRequest roleDTO, Integer userId) {
        Role role = roleRepository.findByName(roleDTO.getName());
        Optional<User> user = userRepository.findById(userId);

        if (role == null || role.getName().equals("USER")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.ROlE_NOT_FOUND)
            );
        }

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.USER_NOT_FOUND)
            );
        }

        Set<Role> userRoles = user.get().getRole();
        userRoles.remove(role);
        user.get().setRole(userRoles);
        userRepository.save(user.get());

        return ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ROLE_REVOKED));
    }
}
