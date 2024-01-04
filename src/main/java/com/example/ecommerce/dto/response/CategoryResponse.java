package com.example.ecommerce.dto.response;

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
    private String slug;
    private String name;
    private boolean status;
    private String description;
    private List<CategoryResponse> sub_categories;
    private Integer parent_category_id;
}
