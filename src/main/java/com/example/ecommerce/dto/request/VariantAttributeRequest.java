package com.example.ecommerce.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class VariantAttributeRequest {
    private Integer id;
    private String name;
    private List<VariantValueRequest> variant_values;
}
