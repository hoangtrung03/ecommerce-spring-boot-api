package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "ec_authority")
@Data
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60, unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}