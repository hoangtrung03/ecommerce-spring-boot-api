package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductDetailResponse;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResultWithPaginationResponse<List<ProductResponse>>> getAllProducts(int page, int size, String sortBy, String sortDirection);

    ResponseEntity<ResultResponse<ProductDetailResponse>> getProduct(Integer id);

    ResponseEntity<ResultResponse<ProductResponse>> addProduct(ProductRequest productRequest);

    ResponseEntity<ResultResponse<ProductResponse>> updateProduct(Integer id, ProductRequest productRequest);

    ResponseEntity<ResultResponse<String>> deleteProduct(Integer id);
}
