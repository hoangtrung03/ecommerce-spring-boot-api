package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT u FROM Category u WHERE CAST(u.id AS string) LIKE %:query% " +
            "OR lower(u.name) LIKE %:query% " +
            "OR lower(u.slug) LIKE %:query%")
    Page<Category> search(@Param("query") String query, Pageable pageable);
}
