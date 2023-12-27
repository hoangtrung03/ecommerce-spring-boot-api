package com.example.ecommerce.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class ErrorModel {
    private String name;
    private String message;

    public ErrorModel(String name, String message) {
        this.name = name.toLowerCase();
        this.message = message;
    }
}
