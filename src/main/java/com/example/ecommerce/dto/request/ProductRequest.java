package com.example.ecommerce.dto.request;

import com.example.ecommerce.validation.ValidProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Slug is required")
    private String slug;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @ValidProductType
    private String type;

    private String description;
    private String summary;
    @NotBlank(message = "Images is required")
    private String images;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price should be a positive value or zero")
    private BigDecimal price;
    private boolean status;
    private Integer category_id;
}
