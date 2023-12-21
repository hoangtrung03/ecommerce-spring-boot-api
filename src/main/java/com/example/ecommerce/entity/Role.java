package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.ecommerce.entity.Permission.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ec_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    @CreationTimestamp
    @Column(name= "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name= "updated_at")
    private Instant updatedAt;

    /**
     * Retrieves the role name.
     *
     * @return  the role name as a string
     */
    public String getRoleName() {
        return name.toString();
    }
}
