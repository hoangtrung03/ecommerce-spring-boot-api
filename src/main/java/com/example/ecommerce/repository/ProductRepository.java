package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE p.slug = :slug AND p.id <> :id")
    Optional<Product> findBySlugAndIdNot(@Param("slug") String slug, @Param("id") Integer id);
}
