package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.ProductType;
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
public class ProductDetailResponse {
    private Integer id;
    private ProductType type;
    private String name;
    private String slug;
    private String description;
    private String summary;
    private String images;
    private BigDecimal price;
    private boolean status;
    private Integer category_id;
    private List<VariantResponse> variants;
}
