package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.EmailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Email;

import java.util.List;

public interface EmailService {
    ResultWithPaginationResponse<List<Email>> getAllEmail(int page, int size, String sortBy, String sortDirection);
    ResultResponse<Email> addEmail(EmailRequest email);
    ResultResponse<Email> updateEmailById(Integer id,EmailRequest email);
    ResultResponse<String> deleteEmailById(Integer id);
    ResultResponse<String> deleteByMultiIds(List<Integer> ids);
}
