package com.oasis.backend.models;

import com.oasis.backend.models.bases.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The Session class represents a user session in the system.
 * It stores information about the user's session, such as authentication level, method, and device details.
 */
@Getter
@Setter
@Entity
@Table(name = "sessions")
public class Session extends BaseEntity {
    @Column(name = "revoked", nullable = false)
    private Boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "user_session_fkey")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}