package com.oasis.backend.repositories;

import com.oasis.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAddressIgnoreCase(String username);

    Optional<User> findBySessions_Id(@NonNull UUID id);
}