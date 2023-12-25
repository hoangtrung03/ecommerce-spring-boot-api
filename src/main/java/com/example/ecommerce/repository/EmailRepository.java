package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    Email findByType(String type);
}
