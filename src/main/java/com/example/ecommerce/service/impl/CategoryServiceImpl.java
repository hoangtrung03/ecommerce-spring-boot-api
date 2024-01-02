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
import java.util.Optional;
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
        var isExistParentId = categoryRepository.findById(categoryRequest.getParentCategoryId());

        if (isExistParentId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.CATEGORY_NOT_FOUND));
        }

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

    @Override
    public ResponseEntity<ResultResponse<String>> deleteCategory(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.CATEGORY_NOT_FOUND));
        }

        Category category = optionalCategory.get();
        Category parentCategory = category.getParentCategory();
        List<Category> subCategories = category.getSubCategories();

        if (parentCategory != null) {
            for (Category subCategory : subCategories) {
                subCategory.setParentCategory(parentCategory);
                categoryRepository.save(subCategory);
            }
            parentCategory.getSubCategories().remove(category);
            categoryRepository.save(parentCategory);
        } else {
            for (Category subCategory : subCategories) {
                subCategory.setParentCategory(null);
                subCategory.getSubCategories().forEach(sc -> sc.setParentCategory(null));
                categoryRepository.saveAll(subCategory.getSubCategories());
            }
        }

        categoryRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_CATEGORY_SUCCESS));
    }

    @Override
    public ResponseEntity<ResultResponse<CategoryResponse>> getCategory(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        return optionalCategory.map(category -> ResponseEntity.status(HttpStatus.OK)
                        .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.GET_CATEGORY_SUCCESS, mapCategoryToResponse(category))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.CATEGORY_NOT_FOUND)));
    }

    @Override
    public ResponseEntity<ResultResponse<CategoryResponse>> updateCategory(Integer id, CategoryRequest categoryRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.CATEGORY_NOT_FOUND));
        }

        Category categoryToUpdate = optionalCategory.get();
        categoryToUpdate.setName(categoryRequest.getName());
        categoryToUpdate.setSlug(categoryRequest.getSlug());
        categoryToUpdate.setDescription(categoryRequest.getDescription());
        categoryToUpdate.setStatus(categoryRequest.isStatus());

        if (categoryRequest.getParentCategoryId() != null) {
            Optional<Category> optionalParentCategory = categoryRepository.findById(categoryRequest.getParentCategoryId());

            if (optionalParentCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.PARENT_CATEGORY_NOT_FOUND));
            }

            categoryToUpdate.setParentCategory(optionalParentCategory.get());
        } else {
            categoryToUpdate.setParentCategory(null);
        }

        categoryRepository.save(categoryToUpdate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_CATEGORY_SUCCESS, mapCategoryToResponse(categoryToUpdate)));
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
