package com.example.ecommerce.dto.response;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultWithPaginationResponse<T> extends ResultResponse<T> {
    private PaginationInfo pagination;

    public ResultWithPaginationResponse(Integer code, String message, T data, PaginationInfo pagination) {
        super(code, message, data);
        this.pagination = pagination;
    }
}
