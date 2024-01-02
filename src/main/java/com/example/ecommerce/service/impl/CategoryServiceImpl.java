package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
        }

        return null;
    }

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getAllCategories(int page, int size, String sortBy, String sortDirection) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResultWithPaginationResponse<>(200, "Success", null, null));
    }
}
