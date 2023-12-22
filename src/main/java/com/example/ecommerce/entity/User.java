package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ec_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @CreationTimestamp
    @Column(name= "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name= "updated_at")
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ec_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> role = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    /**
     * Retrieves the collection of granted authorities for this user.
     *
     * @return the collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the password.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the username.
     *
     * @return the username as a String
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Determines whether the account is non-expired.
     *
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * A description of the entire Java function.
     *
     * @return description of return value
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * A description of the entire Java function.
     *
     * @return The function always returns true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * A description of the entire Java function.
     *
     * @return description of return value
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
