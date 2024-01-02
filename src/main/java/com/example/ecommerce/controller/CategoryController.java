package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.CategoryRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping("/all")
    public ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getCategories(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_direction", required = false) String sortDirection
    ) {
        return categoryService.getAllCategories(page, size, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<CategoryResponse>> getCategory(@PathVariable("id") Integer id) {
        return categoryService.getCategory(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ResultResponse<CategoryResponse>> addCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.addCategory(categoryRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResultResponse<CategoryResponse>> updateCategory(
            @PathVariable("id") Integer id,
            @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultResponse<String>> deleteCategory(@PathVariable("id") Integer id) {
        return categoryService.deleteCategory(id);
    }
}
