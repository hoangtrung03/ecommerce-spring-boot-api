package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private String description;
    private String summary;
    private String images;
    private Double price;
    private Double discountPrice;
    private Integer quantity;
    private CategoryResponse category;
    private ProductStatus status;
}
