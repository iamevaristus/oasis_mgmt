package com.oasis.backend.models;

import com.oasis.backend.models.bases.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "task_categories")
public class TaskCategory extends BaseModel {
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "Task category title cannot be empty")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "task_category_user_fkey")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Task> tasks;
}
