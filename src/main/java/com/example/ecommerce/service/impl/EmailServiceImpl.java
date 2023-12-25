package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.EmailRequest;
import com.example.ecommerce.dto.response.AuthResponse;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Email;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.EmailRepository;
import com.example.ecommerce.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;


    @Override
    public ResultWithPaginationResponse<List<Email>> getAllEmail(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Email> emailPage = emailRepository.findAll(pageable);

        List<Email> emails = emailPage.getContent();

        PaginationInfo paginationInfo = new PaginationInfo(
                emailPage.getNumber(), emailPage.getSize(), emailPage.getTotalPages());
        return new ResultWithPaginationResponse<>(
                StatusCode.SUCCESS,
                "Get all users success",
                emails,
                paginationInfo
        );
    }

    @Override
    public ResultResponse<Email> addEmail(EmailRequest emailRequest) {
        var isExistTypeEmail = emailRepository.findByType(emailRequest.getType());

        if (isExistTypeEmail != null) {
            return new ResultResponse<>(StatusCode.SUCCESS, "Email type already exists", null);
        }

        var email = Email
                .builder()
                .type(emailRequest.getType())
                .content(emailRequest.getContent())
                .status(emailRequest.isStatus())
                .subject(emailRequest.getSubject())
                .build();

        Email savedEmail = emailRepository.save(email);

        return new ResultResponse<>(StatusCode.SUCCESS, "Add email success", savedEmail);
    }

    @Override
    public ResultResponse<Email> updateEmailById(Integer id, EmailRequest emailRequest) {
        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (optionalEmail.isEmpty()) {
            return new ResultResponse<>(StatusCode.NOT_FOUND, "Email not found", null);
        }

        Email emailToUpdate = optionalEmail.get();
        emailToUpdate.setType(emailRequest.getType());
        emailToUpdate.setContent(emailRequest.getContent());
        emailToUpdate.setStatus(emailRequest.isStatus());
        emailToUpdate.setSubject(emailRequest.getSubject());

        Email updatedEmail = emailRepository.save(emailToUpdate);

        return new ResultResponse<>(StatusCode.SUCCESS, "Update email success", updatedEmail);
    }

    @Override
    public ResultResponse<String> deleteEmailById(Integer id) {
        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (optionalEmail.isEmpty()) {
            return new ResultResponse<>(StatusCode.NOT_FOUND, "Email not found", null);
        }

        emailRepository.deleteById(id);
        return new ResultResponse<>(StatusCode.SUCCESS, "Delete email success", null);
    }

    @Override
    public ResultResponse<String> deleteByMultiIds(List<Integer> ids) {
        var isExistIds = emailRepository.findAllById(ids);

        if (isExistIds.isEmpty()) {
            return new ResultResponse<>(StatusCode.NOT_FOUND, "Email not found", null);
        }

        emailRepository.deleteAllById(ids);
        return new ResultResponse<>(StatusCode.SUCCESS, "Delete email success", null);
    }
}
