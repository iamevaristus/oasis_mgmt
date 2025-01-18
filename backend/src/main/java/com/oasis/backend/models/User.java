package com.oasis.backend.models;

import com.oasis.backend.models.bases.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    @Column(name = "email_address", unique = true, nullable = false, columnDefinition = "TEXT")
    @Email(
            message = "Email address must be properly formatted",
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    )
    private String emailAddress;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Column(name = "first_name", columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Session> sessions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return getEmailAddress();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}