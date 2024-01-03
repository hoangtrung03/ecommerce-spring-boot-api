package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.CategoryRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getAllCategoryAdmin(int page, int size, String sortBy, String sortDirection);

    ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getAllCategories(int page, int size, String sortBy, String sortDirection);

    ResponseEntity<ResultResponse<CategoryResponse>> addCategory(CategoryRequest categoryRequest);

    ResponseEntity<ResultResponse<String>> deleteCategory(Integer id);

    ResponseEntity<ResultResponse<CategoryResponse>> getCategory(Integer id);

    ResponseEntity<ResultResponse<CategoryResponse>> updateCategory(Integer id, CategoryRequest categoryRequest);

    ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> searchCategory(String keyword, Integer page, Integer size, String sortBy, String sortDirection);
}
