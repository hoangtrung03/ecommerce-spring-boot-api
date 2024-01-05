package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.request.VariantRequest;
import com.example.ecommerce.dto.response.*;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.SKU;
import com.example.ecommerce.entity.Variant;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<ProductResponse>>> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        PageRequest pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> products = productPage.getContent().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(
                productPage.getNumber(), productPage.getSize(), productPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(new ResultWithPaginationResponse<>(StatusCode.SUCCESS, Messages.GET_ALL_PRODUCT_SUCCESS, products, paginationInfo));
    }

    @Override
    public ResponseEntity<ResultResponse<ProductDetailResponse>> getProduct(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        return optionalProduct.map(product -> ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.GET_PRODUCT_SUCCESS, mapProductDetailToResponse(product))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<ResultResponse<ProductResponse>> addProduct(ProductRequest productRequest) {
        Optional<Product> isExistProductBySlug = productRepository.findBySlug(productRequest.getSlug());

        if (isExistProductBySlug.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResultResponse<>(StatusCode.CONFLICT, Messages.PRODUCT_EXIST, null));
        }

        Product newProduct = mapRequestToProduct(productRequest);

        Optional<Category> category = categoryRepository.findById(productRequest.getCategory_id());
        category.ifPresent(newProduct::setCategory);

        Product savedProduct = productRepository.save(newProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ADD_PRODUCT_SUCCESS, mapProductToResponse(savedProduct)));
    }

    @Override
    public ResponseEntity<ResultResponse<ProductResponse>> updateProduct(Integer id, ProductRequest productRequest) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.PRODUCT_NOT_FOUND, null));
        }

        Product existingProduct = optionalProduct.get();

        if (!existingProduct.getSlug().equals(productRequest.getSlug())) {
            Optional<Product> isExistProductBySlug = productRepository.findBySlugAndIdNot(productRequest.getSlug(), id);

            if (isExistProductBySlug.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResultResponse<>(StatusCode.CONFLICT, Messages.PRODUCT_EXIST, null));
            }
        }

        existingProduct.setName(productRequest.getName());
        existingProduct.setSlug(productRequest.getSlug());
        existingProduct.setDescription(productRequest.getDescription());

        List<Variant> newVariants = productRequest.getVariants().stream()
                .map(this::mapRequestToVariant)
                .toList();
        existingProduct.getVariants().addAll(newVariants);

        Product updatedProduct = productRepository.save(existingProduct);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_PRODUCT_SUCCESS, mapProductToResponse(updatedProduct)));
    }


    @Override
    public ResponseEntity<ResultResponse<String>> deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_PRODUCT_SUCCESS, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.PRODUCT_NOT_FOUND, null));
        }
    }

    private ProductResponse mapProductToResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);

        if (product.getCategory() != null) {
            productResponse.setCategory_id(product.getCategory().getId());
        }

        return productResponse;
    }

    private ProductDetailResponse mapProductDetailToResponse(Product product) {
        ProductDetailResponse productDetailResponse = modelMapper.map(product, ProductDetailResponse.class);

        if (product.getCategory() != null) {
            productDetailResponse.setCategory_id(product.getCategory().getId());
        }

        List<VariantResponse> variantResponses = product.getVariants().stream()
                .map(this::mapVariantToResponse)
                .collect(Collectors.toList());
        productDetailResponse.setVariants(variantResponses);

        return productDetailResponse;
    }

    private Variant mapRequestToVariant(VariantRequest variantRequest) {
        Variant variant = new Variant();
        variant.setName(variantRequest.getName());
        variant.setSlug(variantRequest.getSlug());

        return variant;
    }


    private VariantResponse mapVariantToResponse(Variant variant) {
        VariantResponse variantResponse = modelMapper.map(variant, VariantResponse.class);

        List<SKUResponse> skuResponses = variant.getSkus().stream()
                .map(this::mapSKUToResponse)
                .collect(Collectors.toList());
        variantResponse.setSkus(skuResponses);

        return variantResponse;
    }

    private SKUResponse mapSKUToResponse(SKU sku) {
        return modelMapper.map(sku, SKUResponse.class);
    }

    private Product mapRequestToProduct(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
}
