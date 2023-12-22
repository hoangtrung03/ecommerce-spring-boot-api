package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.RoleRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleService;

    @PostMapping("/create")
    public ResponseEntity<ResultResponse> createRole(@RequestBody RoleRequest roleDTO) {
        ResponseEntity<ResultResponse> result = roleService.createRole(roleDTO);

        return result;
    }

}
