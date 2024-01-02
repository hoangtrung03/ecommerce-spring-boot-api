package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResultWithPaginationResponse<List<ProductResponse>>> getAllProduct(int page, int size, String sortBy, String sortDirection);
}
