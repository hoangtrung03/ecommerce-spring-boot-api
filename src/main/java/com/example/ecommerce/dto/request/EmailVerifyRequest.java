package com.example.ecommerce.dto.request;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String email_verify_token;
}
