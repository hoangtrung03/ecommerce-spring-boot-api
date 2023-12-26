package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.EmailRequest;
import com.example.ecommerce.dto.response.PaginationInfo;
import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.dto.response.ResultWithPaginationResponse;
import com.example.ecommerce.entity.Email;
import com.example.ecommerce.model.Messages;
import com.example.ecommerce.model.StatusCode;
import com.example.ecommerce.repository.EmailRepository;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final MailSenderService mailSender;

    @Override
    public ResponseEntity<ResultWithPaginationResponse<List<Email>>> getAllEmail(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page - 1, size, direction, sortBy);
        Page<Email> emailPage = emailRepository.findAll(pageable);

        List<Email> emails = emailPage.getContent();

        PaginationInfo paginationInfo = new PaginationInfo(
                emailPage.getNumber(), emailPage.getSize(), emailPage.getTotalPages());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultWithPaginationResponse<>(
                        StatusCode.SUCCESS,
                        Messages.GET_ALL_USERS_SUCCESS,
                        emails,
                        paginationInfo
                ));
    }

    @Override
    public ResponseEntity<ResultResponse<Email>> addEmail(EmailRequest emailRequest) {
        var isExistTypeEmail = emailRepository.findByType(emailRequest.getType());

        if (isExistTypeEmail != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.EMAIL_TYPE_ALREADY_EXISTS, null));
        }

        var email = Email
                .builder()
                .type(emailRequest.getType())
                .content(emailRequest.getContent())
                .status(emailRequest.isStatus())
                .subject(emailRequest.getSubject())
                .build();

        Email savedEmail = emailRepository.save(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.ADD_EMAIL_SUCCESS, savedEmail));
    }

    @Override
    public ResponseEntity<ResultResponse<Email>> updateEmailById(Integer id, EmailRequest emailRequest) {
        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (optionalEmail.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.EMAIL_NOT_FOUND, null));
        }

        Email emailToUpdate = optionalEmail.get();
        emailToUpdate.setType(emailRequest.getType());
        emailToUpdate.setContent(emailRequest.getContent());
        emailToUpdate.setStatus(emailRequest.isStatus());
        emailToUpdate.setSubject(emailRequest.getSubject());

        Email updatedEmail = emailRepository.save(emailToUpdate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.UPDATE_EMAIL_SUCCESS, updatedEmail));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> deleteEmailById(Integer id) {
        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (optionalEmail.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.EMAIL_NOT_FOUND, null));
        }

        emailRepository.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_EMAIL_SUCCESS, null));
    }

    @Override
    public ResponseEntity<ResultResponse<String>> deleteByMultiIds(List<Integer> ids) {
        var isExistIds = emailRepository.findAllById(ids);

        if (isExistIds.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(StatusCode.NOT_FOUND, Messages.EMAIL_NOT_FOUND, null));
        }

        emailRepository.deleteAllById(ids);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultResponse<>(StatusCode.SUCCESS, Messages.DELETE_EMAIL_SUCCESS, null));
    }

    @Override
    public void sendVerificationEmail(String fromAddress, String fromName, String toAddress, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, fromName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.sendNewMail(toAddress, subject, content);
    }
}
