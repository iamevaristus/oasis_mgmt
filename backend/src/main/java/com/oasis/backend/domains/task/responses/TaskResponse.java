package com.oasis.backend.domains.task.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oasis.backend.enums.TaskPriority;
import com.oasis.backend.enums.TaskStatus;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;

    @JsonProperty("due_date")
    private Date dueDate;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("updated_at")
    private ZonedDateTime updatedAt;
}