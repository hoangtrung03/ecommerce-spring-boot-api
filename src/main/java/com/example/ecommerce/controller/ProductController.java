package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;

    @GetMapping("/all")
    public ResponseEntity<ResultWithPaginationResponse<List<ProductResponse>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_direction", required = false) String sortDirection
    ) {
        return productService.getAllProduct(page, size, sortBy, sortDirection);
    }
}
