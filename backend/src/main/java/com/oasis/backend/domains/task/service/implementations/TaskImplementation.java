package com.oasis.backend.domains.task.service.implementations;

import com.oasis.backend.configurations.exceptions.OasisException;
import com.oasis.backend.core.mappers.TaskMapper;
import com.oasis.backend.domains.task.dto.TaskDto;
import com.oasis.backend.domains.task.responses.TaskCategoryResponse;
import com.oasis.backend.domains.task.responses.TaskResponse;
import com.oasis.backend.domains.task.service.TaskService;
import com.oasis.backend.models.Task;
import com.oasis.backend.models.TaskCategory;
import com.oasis.backend.models.bases.ApiResponse;
import com.oasis.backend.repositories.TaskCategoryRepository;
import com.oasis.backend.repositories.TaskRepository;
import com.oasis.backend.utils.TimeUtil;
import com.oasis.backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskImplementation implements TaskService {
    private final String DEFAULT_CATEGORY = "All";

    private final TaskMapper taskMapper;
    private final UserUtil userUtil;
    private final TaskCategoryRepository taskCategoryRepository;
    private final TaskRepository taskRepository;

    @Override
    public ApiResponse<TaskResponse> create(TaskDto taskDto) {
        TaskResponse response ;

        if(taskDto.getCategory() == null || taskDto.getCategory().isEmpty()) {
            response = taskMapper.toResponse(createTaskWithDefaultCategory(taskDto));
        } else {
            response = taskMapper.toResponse(createTask(taskDto));
        }

        return new ApiResponse<>("Task successfully created", response, HttpStatus.CREATED);
    }

    private Task createTaskWithDefaultCategory(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setCategory(getOrCreateDefaultCategory(DEFAULT_CATEGORY));
        task.setUser(userUtil.getUser());

        return taskRepository.save(task);
    }

    private TaskCategory getOrCreateDefaultCategory(String title) {
        return taskCategoryRepository.findByTitleIgnoreCaseAndUserId(title, userUtil.getUser().getId())
                .orElseGet(() -> {
                    TaskCategory newCategory = new TaskCategory();
                    newCategory.setTitle(title);
                    newCategory.setUser(userUtil.getUser());
                    return taskCategoryRepository.save(newCategory);
                });
    }

    private Task createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        task.setCategory(getOrCreateDefaultCategory(taskDto.getCategory()));
        task.setUser(userUtil.getUser());

        return taskRepository.save(task);
    }

    @Override
    public ApiResponse<TaskResponse> update(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new OasisException("Task not found"));

        if(task.getUser().getId().equals(userUtil.getUser().getId())) {
            taskMapper.update(taskDto, task);
            task.setUpdatedAt(TimeUtil.now());
            task = taskRepository.save(task);

            if(taskDto.getCategory() != null && !taskDto.getCategory().isEmpty()) {
                task.getCategory().setTitle(taskDto.getCategory());
                task.getCategory().setUpdatedAt(TimeUtil.now());
                taskCategoryRepository.save(task.getCategory());
            }

            return new ApiResponse<>(taskMapper.toResponse(task));
        } else {
            throw new OasisException("User not authorized to update this task");
        }
    }

    @Override
    @Transactional
    public ApiResponse<List<TaskResponse>> delete(Long id, String category) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new OasisException("Task not found"));

        if(task.getUser().getId().equals(userUtil.getUser().getId())) {
            if(category == null || category.isEmpty()) {
                category = task.getCategory().getTitle();
            }
            taskRepository.delete(task);

            return new ApiResponse<>(
                    String.format("Successfully deleted task from %s", category),
                    getTasks(category, null, null),
                    HttpStatus.OK
            );
        } else {
            throw new OasisException("User not authorized to delete this task");
        }
    }

    private List<TaskResponse> getTasks(String category, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.Direction.ASC,
                "dueDate", "priority", "createdAt"
        );
        Page<Task> tasks = taskRepository.findByCategoryTitleIgnoreCase(category, userUtil.getUser().getId(), pageable);

        if(tasks.hasContent()) {
            return tasks.map(taskMapper::toResponse).stream().toList();
        }

        return List.of();
    }

    @Override
    public ApiResponse<List<TaskCategoryResponse>> get(Integer page, Integer size) {
        List<TaskCategory> categories = taskCategoryRepository.findByUser_Id(userUtil.getUser().getId());

        if(categories.isEmpty()) {
            return new ApiResponse<>(List.of());
        }

        List<TaskCategoryResponse> list = categories.stream()
                .map(taskCategory -> {
                    TaskCategoryResponse response = taskMapper.toCategoryResponse(taskCategory);
                    response.setTasks(getTasks(taskCategory.getTitle(), page, size));

                    return response;
                })
                .sorted(Comparator.comparing(category -> category.getTitle().equalsIgnoreCase(DEFAULT_CATEGORY) ? 0 : 1))
                .toList();

        return new ApiResponse<>(list);
    }

    @Override
    public ApiResponse<List<TaskResponse>> search(String query, String category, Integer page, Integer size) {
        if(category == null || category.isEmpty()) {
            category = DEFAULT_CATEGORY;
        }

        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 20);
        Page<Task> tasks = taskRepository.fullTextSearch(query, userUtil.getUser().getId(), category, pageable);

        if(tasks.hasContent()) {
            return new ApiResponse<>(tasks.map(taskMapper::toResponse).stream().toList());
        }

        return new ApiResponse<>(List.of());
    }
}