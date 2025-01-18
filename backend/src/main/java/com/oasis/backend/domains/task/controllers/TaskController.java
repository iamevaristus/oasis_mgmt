package com.oasis.backend.domains.task.controllers;

import com.oasis.backend.domains.task.dto.TaskDto;
import com.oasis.backend.domains.task.responses.TaskCategoryResponse;
import com.oasis.backend.domains.task.responses.TaskResponse;
import com.oasis.backend.domains.task.service.TaskService;
import com.oasis.backend.models.bases.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskCategoryResponse>>> get(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        ApiResponse<List<TaskCategoryResponse>> response = service.get(page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> search(
            @RequestParam String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        ApiResponse<List<TaskResponse>> response = service.search(query, category, page, size);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskResponse>> create(@RequestBody TaskDto dto) {
        ApiResponse<TaskResponse> response = service.create(dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(@RequestBody TaskDto dto, @PathVariable("id") Long id) {
        ApiResponse<TaskResponse> response = service.update(id, dto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> delete(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String category
    ) {
        ApiResponse<List<TaskResponse>> response = service.delete(id, category);
        return new ResponseEntity<>(response, response.getStatus());
    }
}