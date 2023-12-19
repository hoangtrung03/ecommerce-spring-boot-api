package com.example.ecommerce.dto.response;

import com.example.ecommerce.model.ErrorModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseModel {
    public String type;
    public ErrorModel errors;
}
