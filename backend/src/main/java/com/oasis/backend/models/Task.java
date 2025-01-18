package com.oasis.backend.models;

import com.oasis.backend.enums.TaskPriority;
import com.oasis.backend.enums.TaskStatus;
import com.oasis.backend.models.bases.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends BaseModel {
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Task title cannot be empty")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Task description cannot be empty")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.HIGH;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "due_date", nullable = false)
    @FutureOrPresent(message = "Task due date must be in the future or present")
    private Date dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "task_category_fkey")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "task_user_fkey")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}