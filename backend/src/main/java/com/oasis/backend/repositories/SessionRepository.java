package com.oasis.backend.repositories;

import com.oasis.backend.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Query("select s from Session s where s.user.id = ?1 and s.revoked = false")
    List<Session> findAllUserNonRevoked(@NonNull UUID id);
}