package com.example.ecommerce.validation;

import com.example.ecommerce.entity.ProductType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ProductTypeValidator implements ConstraintValidator<ValidProductType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(ProductType.values())
                .anyMatch(enumValue -> enumValue.name().equals(value));
    }
}
