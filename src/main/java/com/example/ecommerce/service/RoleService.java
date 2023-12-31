package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<ResultResponse<Object>> getAllRoles(Integer page, Integer size, String sortBy, String sortDirection);
    ResponseEntity<ResultResponse<Object>> getRoleById(Integer roleId);
    ResponseEntity<ResultResponse<Object>> createRole(RoleRequest roleDTO);
    ResponseEntity<ResultResponse<Object>> assignRole(RoleRequest roleDTO, Integer userId);
    ResponseEntity<ResultResponse<Object>> revokeRole(RoleRequest roleDTO, Integer userId);
    ResponseEntity<ResultResponse<Object>> deleteRole(Integer roleId);
    ResponseEntity<ResultResponse<Object>> updateRole(Integer id, RoleRequest roleDTO);
}
