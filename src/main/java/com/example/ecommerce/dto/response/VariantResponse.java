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
    private BigDecimal discount_rate;
    private BigDecimal discounted_price;
    private Integer quantity;
    private ProductStatus status;
    private List<VariantAttributeResponse> variant_attributes;
    private List<SKUResponse> skus;
}
