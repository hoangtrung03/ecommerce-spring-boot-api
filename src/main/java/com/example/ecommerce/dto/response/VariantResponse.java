package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantResponse {
    private Integer id;
    private String name;
    private String slug;
    private String images;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private BigDecimal discountRate;
    private Integer quantity;
    private ProductStatus status;
    private List<VariantAttributeResponse> variantAttributes;
    private List<SKUResponse> skus;
}
