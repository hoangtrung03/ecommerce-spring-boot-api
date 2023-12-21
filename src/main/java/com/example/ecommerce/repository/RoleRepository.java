package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
    boolean existsByName(RoleName name);
}
