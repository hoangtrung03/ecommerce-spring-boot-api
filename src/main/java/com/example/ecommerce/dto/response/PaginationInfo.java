package com.example.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class PaginationInfo {
    private int page;
    private int size;
    private int total_page;

    public PaginationInfo(int page, int size, int total_page) {
        this.page = page + 1;
        this.size = size;
        this.total_page = total_page;
    }
}
