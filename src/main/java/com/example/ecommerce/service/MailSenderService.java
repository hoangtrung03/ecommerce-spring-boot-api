package com.example.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;


public interface MailSenderService {
    void sendNewMail(String to, String subject, String body) throws MessagingException;

    MimeMessage createMimeMessage();
}
