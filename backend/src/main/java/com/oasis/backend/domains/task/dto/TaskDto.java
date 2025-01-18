package com.oasis.backend.domains.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oasis.backend.enums.TaskPriority;
import com.oasis.backend.enums.TaskStatus;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private String category;

    @JsonProperty("due_date")
    private Date dueDate;
}
