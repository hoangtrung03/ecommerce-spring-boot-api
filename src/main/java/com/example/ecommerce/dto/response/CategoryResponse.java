package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Integer id;
    private List<Category> subCategories;
    private String slug;
    private String name;
    private String description;
}
