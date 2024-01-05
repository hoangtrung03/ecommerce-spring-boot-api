package com.example.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductType {
    String message() default "Type must be in SINGLE, VARIANTS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
