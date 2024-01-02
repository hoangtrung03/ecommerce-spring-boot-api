package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.CategoryRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<CategoryResponse>>> getAllCategories(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryResponse> categoryResponses = categoryPage.stream()
                .filter(category -> category.getParentCategory() == null)
                .map(this::mapCategoryToResponse)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                categoryPage.getNumber(), categoryPage.getSize(), categoryPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_CATEGORY_SUCCESS, categoryResponses, paginationInfo));
    }

    @Override
    public ResponseEntity<ResultResponse<CategoryResponse>> addCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .slug(categoryRequest.getSlug())
                .description(categoryRequest.getDescription())
                .status(categoryRequest.isStatus())
                .parentCategory(categoryRepository.findById(categoryRequest.getParentCategoryId()).orElse(null))
                .build();

        categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ADD_CATEGORY_SUCCESS));
    }

    private CategoryResponse mapCategoryToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .status(category.isStatus())
                .subCategories(mapSubCategories(category.getSubCategories()))
                .build();
    }

    private List<CategoryResponse> mapSubCategories(List<Category> subCategories) {
        if (subCategories == null || subCategories.isEmpty()) {
            return null;
        }
        return subCategories.stream()
                .map(this::mapCategoryToResponse)
                .collect(Collectors.toList());
    }
}