package com.example.ecommerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VariantRequest {
    @NotBlank(message = "Name Variant is required")
    private String name;

    @NotBlank(message = "Slug Variant is required")
    private String slug;
    private String images;

    @PositiveOrZero(message = "Price Variant is required")
    private BigDecimal price;

    @PositiveOrZero(message = "Discount Rate Variant is required")
    private BigDecimal discount_rate;
    @PositiveOrZero(message = "Discounted Price Variant is required")
    private BigDecimal discounted_price;

    private Integer quantity;

    @NotNull(message = "Variants Attributes is required")
    @Size(min = 1, message = "Variants Attributes is required")
    @Valid
    private List<VariantAttributeRequest> variant_attributes;

    //    @NotNull(message = "SKU is required")
//    @Size(min = 1, message = "SKU is required")
    @Valid
    private List<SKURequest> skus;
}
