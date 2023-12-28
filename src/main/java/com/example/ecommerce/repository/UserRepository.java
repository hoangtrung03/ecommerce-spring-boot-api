package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findByRole(Role role);
    @Query("SELECT u FROM User u WHERE CAST(u.id AS string) LIKE %:query% " +
        "OR u.email LIKE %:query% " +
        "OR u.firstname LIKE %:query%" +
        "OR u.lastname LIKE %:query%")
    Page<User> search(@Param("query") String query, Pageable pageable);
}
