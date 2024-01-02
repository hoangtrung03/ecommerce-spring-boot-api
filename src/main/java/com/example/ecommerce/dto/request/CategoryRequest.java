package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "This field is required")
    private String name;

    @NotBlank(message = "This field is required")
    private String slug;
    private String description;
    private boolean status;
    private Integer parentCategoryId;
}
