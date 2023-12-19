package com.example.ecommerce.dto.request;

import com.example.ecommerce.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;

    @NotEmpty(message = "This field is required")
    @Email(message = "Email is invalid")

    private String email;

    @NotBlank(message = "This field is required")
    @Size(min = 6, max = 20, message = "Password must be from 6 to 20 characters")
    private String password;
}
