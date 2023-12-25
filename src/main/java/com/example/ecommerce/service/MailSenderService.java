package com.example.ecommerce.service;

public interface MailSenderService {
    void sendNewMail(String to, String subject, String body);
}
