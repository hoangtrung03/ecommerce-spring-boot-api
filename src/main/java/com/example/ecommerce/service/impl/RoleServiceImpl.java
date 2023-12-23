package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

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
                    .body(new ResultResponse<Object>(StatusCode.BAD_REQUEST, "Role name cannot be null or empty"));
        }

        Role role = new Role();
        role.setName(roleName.getName());
        roleRepository.save(role);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<Object>(StatusCode.SUCCESS, "Role created successfully", roleName.getName()));
    }
}
