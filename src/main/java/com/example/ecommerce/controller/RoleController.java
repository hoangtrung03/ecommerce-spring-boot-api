package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleService;

    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Object>> createRole(@RequestBody RoleRequest roleDTO) {
        return roleService.createRole(roleDTO);
    }

    @PutMapping("/assign/{id}")
    public ResponseEntity<ResultResponse<Object>> assignRole(@RequestBody RoleRequest roleDTO, @PathVariable("id") Integer userId) {
        return roleService.assignRole(roleDTO, userId);
    }

    @PutMapping("/revoke/{id}")
    public ResponseEntity<ResultResponse<Object>> revokeRole(@RequestBody RoleRequest roleDTO, @PathVariable("id") Integer userId) {
        return roleService.revokeRole(roleDTO, userId);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ResultResponse<Object>> deleteRole(@PathVariable("id") Integer roleId) {
//        return ResponseEntity.ok(roleService.deleteRole(roleId).getBody());
//    }
}
