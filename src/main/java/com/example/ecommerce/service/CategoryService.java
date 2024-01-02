package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    CategoryResponse getCategoryById(Integer categoryId);

    ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getAllCategories(int page, int size, String sortBy, String sortDirection);
}
