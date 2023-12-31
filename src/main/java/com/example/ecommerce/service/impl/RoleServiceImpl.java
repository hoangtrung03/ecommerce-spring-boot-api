package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.dto.response.RoleResponse;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ResultResponse<Object>> getAllRoles(Integer page, Integer size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Role> rolePage = roleRepository.findAll(pageable);

        List<RoleResponse> roleResponses = rolePage.getContent().stream()
                .map(role -> new RoleResponse(role.getId(), role.getName()))
                .toList();

        PaginationInfo paginationInfo = new PaginationInfo(
                rolePage.getNumber(), rolePage.getSize(), rolePage.getTotalPages());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_ROLE_SUCCESS, roleResponses, paginationInfo));
    }

    @Override
    public ResponseEntity<ResultResponse<Object>> getRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);

        if (role == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.ROlE_NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.GET_ROLE_SUCCESS, new RoleResponse(role.getId(), role.getName())));
    }

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
        Role isExistRole = roleRepository.findByName(roleName.getName());

        if (isExistRole != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<Object>(StatusCode.BAD_REQUEST, Messages.ROLE_ALREADY_EXISTS));
        }

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

    @Override
    public ResponseEntity<ResultResponse<Object>> deleteRole(Integer roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);

        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.ROlE_NOT_FOUND)
            );
        }

        if (role.getName().equals("USER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResultResponse<>(StatusCode.BAD_REQUEST, Messages.ROLE_NOT_DELETABLE)
            );
        }

        List<User> usersWithRole = userRepository.findByRole(role);

        for (User user : usersWithRole) {
            Set<Role> userRoles = user.getRole();
            userRoles.remove(role);
            user.setRole(userRoles);
            userRepository.save(user);
        }

        roleRepository.deleteById(roleId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ROLE_DELETED));
    }

    @Override
    public ResponseEntity<ResultResponse<Object>> updateRole(Integer id, RoleRequest roleDTO) {
        Role role = roleRepository.findById(id).orElse(null);

        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResultResponse<>(StatusCode.NOT_FOUND, Messages.ROlE_NOT_FOUND)
            );
        }

        if (role.getName().equals("USER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResultResponse<>(StatusCode.BAD_REQUEST, Messages.ROLE_NOT_UPDATABLE)
            );
        }

        if (roleRepository.findByName(roleDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResultResponse<>(StatusCode.BAD_REQUEST, Messages.ROLE_ALREADY_EXISTS)
            );
        }

        role.setName(roleDTO.getName());
        roleRepository.save(role);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ROLE_UPDATED));
    }
}
