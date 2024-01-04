package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<ProductResponse>>> getAllProduct(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponse = productPage.getContent().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getSummary(),
                        null,
                        product.isStatus()
                ))
                .toList();

        PaginationInfo paginationInfo = new PaginationInfo(
                productPage.getNumber(), productPage.getSize(), productPage.getTotalPages());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_PRODUCT_SUCCESS, productResponse, paginationInfo));
    }
}
