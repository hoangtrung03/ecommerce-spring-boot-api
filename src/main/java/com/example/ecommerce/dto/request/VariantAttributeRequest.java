package com.example.ecommerce.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class VariantAttributeRequest {
    private String name;
    private List<VariantValueRequest> variantValues;
}
