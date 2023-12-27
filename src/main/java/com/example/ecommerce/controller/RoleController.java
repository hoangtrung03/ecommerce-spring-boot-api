package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.service.impl.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleService;

    @GetMapping("/all")
    public ResponseEntity<ResultResponse<Object>> getAllRoles(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_direction", required = false) String sortDirection
    ) {
        return roleService.getAllRoles(page, size, sortBy, sortDirection);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResultResponse<Object>> getRoleById(@PathVariable("id") Integer roleId) {
        return roleService.getRoleById(roleId);
    }

    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Object>> createRole(@RequestBody @Valid RoleRequest roleDTO) {
        return roleService.createRole(roleDTO);
    }

    @PutMapping("/assign/{id}")
    public ResponseEntity<ResultResponse<Object>> assignRole(@RequestBody @Valid RoleRequest roleDTO, @PathVariable("id") Integer userId) {
        return roleService.assignRole(roleDTO, userId);
    }

    @PutMapping("/revoke/{id}")
    public ResponseEntity<ResultResponse<Object>> revokeRole(@RequestBody @Valid RoleRequest roleDTO, @PathVariable("id") Integer userId) {
        return roleService.revokeRole(roleDTO, userId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultResponse<Object>> deleteRole(@PathVariable("id") Integer roleId) {
        return ResponseEntity.ok(roleService.deleteRole(roleId).getBody());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResultResponse<Object>> updateRole(@PathVariable("id") Integer id, @RequestBody @Valid RoleRequest roleDTO) {
        return roleService.updateRole(id, roleDTO);
    }
}
