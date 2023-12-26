package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.EmailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Email;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface EmailService {
    ResponseEntity<ResultWithPaginationResponse<List<Email>>> getAllEmail(int page, int size, String sortBy, String sortDirection);
    ResponseEntity<ResultResponse<Email>> addEmail(EmailRequest email);
    ResponseEntity<ResultResponse<Email>> updateEmailById(Integer id,EmailRequest email);
    ResponseEntity<ResultResponse<String>> deleteEmailById(Integer id);
    ResponseEntity<ResultResponse<String>> deleteByMultiIds(List<Integer> ids);
    void sendVerificationEmail(String fromAddress, String fromName, String toAddress, String subject, String content) throws MessagingException, UnsupportedEncodingException;
}
