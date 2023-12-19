package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "This field is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "This field is required")
    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    String password;
}
