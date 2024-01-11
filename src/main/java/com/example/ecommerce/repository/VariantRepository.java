package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Integer> {
}
