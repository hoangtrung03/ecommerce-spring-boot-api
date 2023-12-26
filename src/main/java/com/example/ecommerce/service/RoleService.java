package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<ResultResponse<Object>> createRole(RoleRequest roleDTO);
    ResponseEntity<ResultResponse<Object>> assignRole(RoleRequest roleDTO, Integer userId);
    ResponseEntity<ResultResponse<Object>> revokeRole(RoleRequest roleDTO, Integer userId);
}
