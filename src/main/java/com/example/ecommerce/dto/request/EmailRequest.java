package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailRequest {
    @NotBlank(message = "This field is required")
    @Size(max = 100, message = "Type must be maximum 100 characters")
    private String type;
    private boolean status;
    private String subject;
    private String content;
}
