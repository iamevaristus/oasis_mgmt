package com.oasis.backend.core.mappers;

import com.oasis.backend.domains.task.dto.TaskDto;
import com.oasis.backend.domains.task.responses.TaskCategoryResponse;
import com.oasis.backend.domains.task.responses.TaskResponse;
import com.oasis.backend.models.Task;
import com.oasis.backend.models.TaskCategory;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    @Mapping(target = "category", source = "category", ignore = true)
    Task toEntity(TaskDto taskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", source = "category", ignore = true)
    void update(TaskDto taskDto, @MappingTarget Task task);

    TaskResponse toResponse(Task task);

    @Mapping(target = "tasks", source = "tasks", ignore = true)
    TaskCategoryResponse toCategoryResponse(TaskCategory taskCategory);
}