package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.EmailRequest;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Email;
import com.example.ecommerce.service.impl.EmailServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailServiceImpl emailService;

    @GetMapping("/all")
    public ResponseEntity<ResultWithPaginationResponse<List<Email>>> getAllEmails(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", defaultValue = "10") Integer size,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_direction", required = false) String sortDirection
    ) {
        return emailService.getAllEmail(page, size, sortBy, sortDirection);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResultResponse<Email>> getEmailById(@PathVariable("id") Integer id) {
        return emailService.getEmailById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ResultResponse<Email>> addEmail(@Valid @RequestBody EmailRequest email) {
        return emailService.addEmail(email);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResultResponse<Email>> updateEmail(@PathVariable("id") Integer id, @Valid @RequestBody EmailRequest email) {
        return emailService.updateEmailById(id, email);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultResponse<String>> deleteEmail(@PathVariable("id") Integer id) {
        return emailService.deleteEmailById(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<String>> deleteEmails(@RequestParam("ids") List<Integer> ids) {
        return emailService.deleteByMultiIds(ids);
    }
}
