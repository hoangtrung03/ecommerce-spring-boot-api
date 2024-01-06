package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.request.VariantRequest;
import com.example.ecommerce.dto.response.*;
import com.example.ecommerce.entity.*;
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

import java.math.BigDecimal;
import java.util.ArrayList;
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
        Sort.Direction direction = sortDirection != null && sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

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

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.PRODUCT_NOT_FOUND, null));
        }

        return optionalProduct.map(product -> ResponseEntity.status(HttpStatus.OK).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.GET_PRODUCT_SUCCESS, mapProductDetailToResponse(product))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<ResultResponse<?>> addProduct(ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findBySlug(productRequest.getSlug());

        if (existingProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResultResponse<>(StatusCode.CONFLICT, Messages.PRODUCT_EXIST, null));
        }

        ProductType requestType = ProductType.valueOf(productRequest.getType());

        switch (requestType) {
            case SINGLE -> {
                Product newSingleProduct = mapRequestToProduct(productRequest);
                Optional<Category> singleProductCategory = categoryRepository.findById(productRequest.getCategory_id());
                singleProductCategory.ifPresent(newSingleProduct::setCategory);
                Product savedSingleProduct = productRepository.save(newSingleProduct);

                return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ADD_PRODUCT_SUCCESS, mapProductToResponse(savedSingleProduct)));
            }
            case VARIANTS -> {
                Product newVariantProduct = mapRequestToProduct(productRequest);
                Optional<Category> variantProductCategory = categoryRepository.findById(productRequest.getCategory_id());
                variantProductCategory.ifPresent(newVariantProduct::setCategory);

                if (productRequest.getVariants() != null && !productRequest.getVariants().isEmpty()) {
                    List<Variant> newVariants = productRequest.getVariants().stream()
                            .map(variantRequest -> mapRequestToVariant(variantRequest, newVariantProduct))
                            .toList();
                    newVariantProduct.setVariants(newVariants);
                }

                Product savedVariantProduct = productRepository.save(newVariantProduct);

                return ResponseEntity.status(HttpStatus.CREATED).body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ADD_PRODUCT_SUCCESS, mapProductDetailToResponse(savedVariantProduct)));
            }
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResultResponse<>(StatusCode.BAD_REQUEST, Messages.INVALID_PRODUCT_TYPE, null));
            }
        }
    }

    @Override
    public ResponseEntity<ResultResponse<?>> updateProduct(Integer id, ProductRequest productRequest) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.PRODUCT_NOT_FOUND, null));
        }

        Product existingProduct = optionalProduct.get();
        ProductType requestType = ProductType.valueOf(productRequest.getType());

        if (!existingProduct.getSlug().equals(productRequest.getSlug())) {
            Optional<Product> isExistProductBySlug = productRepository.findBySlugAndIdNot(productRequest.getSlug(), id);

            if (isExistProductBySlug.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResultResponse<>(StatusCode.CONFLICT, Messages.PRODUCT_EXIST, null));
            }
        }

        existingProduct.setName(productRequest.getName());
        existingProduct.setSlug(productRequest.getSlug());
        existingProduct.setType(requestType);
        existingProduct.setDescription(productRequest.getDescription());

        switch (requestType) {
            case SINGLE -> {
                Product updatedProduct = productRepository.save(existingProduct);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_PRODUCT_SUCCESS, mapProductToResponse(updatedProduct)));
            }
            case VARIANTS -> {
                List<Variant> variantsToUpdate = new ArrayList<>();

                if (productRequest.getVariants() != null && !productRequest.getVariants().isEmpty()) {
                    variantsToUpdate = productRequest.getVariants().stream()
                            .map(variantRequest -> mapRequestToVariant(variantRequest, existingProduct))
                            .collect(Collectors.toList());
                }

                existingProduct.getVariants().clear();
                existingProduct.getVariants().addAll(variantsToUpdate);

                productRepository.save(existingProduct);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_PRODUCT_SUCCESS, mapProductDetailToResponse(existingProduct)));
            }
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResultResponse<>(StatusCode.BAD_REQUEST, Messages.INVALID_PRODUCT_TYPE, null));
            }
        }
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

        if (product.getType() == ProductType.VARIANTS) {
            List<VariantResponse> variantResponses = product.getVariants().stream()
                    .map(this::mapVariantToResponse)
                    .collect(Collectors.toList());
            productDetailResponse.setVariants(variantResponses);
        }

        if (product.getCategory() != null) {
            productDetailResponse.setCategory_id(product.getCategory().getId());
        }

        return productDetailResponse;
    }

    private Variant mapRequestToVariant(VariantRequest variantRequest, Product product) {
        Variant variant = modelMapper.map(variantRequest, Variant.class);
        variant.setProduct(product);
        variant.setDiscountRate(variantRequest.getDiscount_rate());

        BigDecimal discountedPrice = variantRequest.getPrice().subtract(variantRequest.getPrice().multiply(variantRequest.getDiscount_rate().divide(BigDecimal.valueOf(100))));
        variant.setDiscountedPrice(discountedPrice);

        if (variant.getQuantity() > 0) {
            variant.setStatus(ProductStatus.AVAILABLE);
        } else {
            variant.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        return variant;
    }

    private VariantResponse mapVariantToResponse(Variant variant) {
        VariantResponse variantResponse = modelMapper.map(variant, VariantResponse.class);
        variantResponse.setDiscount_rate(variant.getDiscountRate());
        variantResponse.setDiscounted_price(variant.getDiscountedPrice());

        if (variant.getVariantAttributes() != null) {
            List<VariantAttributeResponse> variantAttributeResponses = variant.getVariantAttributes().stream()
                    .map(this::mapVariantAttributeToResponse)
                    .toList();
            variantResponse.setVariant_attributes(variantAttributeResponses);
        }

        if (variant.getSkus() != null) {
            List<SKUResponse> skuResponses = variant.getSkus().stream()
                    .map(this::mapSKUToResponse)
                    .collect(Collectors.toList());
            variantResponse.setSkus(skuResponses);
        }

        return variantResponse;
    }

    private VariantAttributeResponse mapVariantAttributeToResponse(VariantAttribute variantAttribute) {
        VariantAttributeResponse variantAttributeResponse = new VariantAttributeResponse();
        variantAttributeResponse.setName(variantAttribute.getName());

        if (variantAttribute.getVariantValues() != null) {
            List<VariantValueResponse> variantValueResponses = variantAttribute.getVariantValues().stream()
                    .map(this::mapVariantValueToResponse)
                    .toList();

            variantAttributeResponse.setVariant_values(variantValueResponses);
        }

        return variantAttributeResponse;
    }

    private VariantValueResponse mapVariantValueToResponse(VariantValue variantAttribute) {
        return VariantValueResponse.builder().id(variantAttribute.getId()).name(variantAttribute.getName()).build();
    }

    private SKUResponse mapSKUToResponse(SKU sku) {
        return modelMapper.map(sku, SKUResponse.class);
    }

    private Product mapRequestToProduct(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
}
