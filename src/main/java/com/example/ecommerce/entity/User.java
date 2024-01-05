package com.example.ecommerce.entity;

import com.example.ecommerce.model.UserVerifyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ec_user")
@ToString(exclude = {"tokens", "role"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String firstname;

    @Column(nullable = true)
    private String lastname;
    private String email;
    private String password;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ec_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> role = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Token> tokens;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date date_of_birth;

    @Column(nullable = true)
    private String bio;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String country;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String gender;

    private UserVerifyStatus userVerifyStatus;

    @Column(name = "email_verify_token")
    private String verificationCode;

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
