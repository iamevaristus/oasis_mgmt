package com.oasis.backend.domains.task.service;

import com.oasis.backend.domains.task.dto.TaskDto;
import com.oasis.backend.domains.task.responses.TaskCategoryResponse;
import com.oasis.backend.domains.task.responses.TaskResponse;
import com.oasis.backend.models.bases.ApiResponse;

import java.util.List;

/**
 * Interface defining the task service.
 */
public interface TaskService {
    /**
     * Creates a new task.
     *
     * @param taskDto The task data.
     * @return An ApiResponse containing the task response.
     */
    ApiResponse<TaskResponse> create(TaskDto taskDto);

    /**
     * Updates an existing task.
     *
     * @param id The ID of the task to update.
     * @param taskDto The updated task data.
     * @return An ApiResponse containing the task response.
     */
    ApiResponse<TaskResponse> update(Long id, TaskDto taskDto);

    /**
     * Deletes tasks based on the given ID and category.
     *
     * @param id The ID of the task to delete (optional).
     * @param category The category of the tasks to delete (optional).
     * @return An ApiResponse containing a list of deleted task responses.
     */
    ApiResponse<List<TaskResponse>> delete(Long id, String category);

    /**
     * Retrieves a paginated list of categorized tasks.
     *
     * @param page The page number.
     * @param size The page size.
     * @return An ApiResponse containing a list of categorized task responses.
     */
    ApiResponse<List<TaskCategoryResponse>> get(Integer page, Integer size);

    /**
     * Searches for tasks based on the given query and category.
     *
     * @param query The search query.
     * @param category The category to filter by (optional).
     * @param page The page number.
     * @param size The page size.
     * @return An ApiResponse containing a list of task responses.
     */
    ApiResponse<List<TaskResponse>> search(String query, String category, Integer page, Integer size);
}