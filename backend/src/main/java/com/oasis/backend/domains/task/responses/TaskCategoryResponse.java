package com.oasis.backend.domains.task.responses;

import lombok.Data;

import java.util.List;

@Data
public class TaskCategoryResponse {
    private Long id;
    private String title;
    private List<TaskResponse> tasks;
}