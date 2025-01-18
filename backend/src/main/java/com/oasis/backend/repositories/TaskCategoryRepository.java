package com.oasis.backend.repositories;

import com.oasis.backend.models.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
  Optional<TaskCategory> findByTitleIgnoreCaseAndUserId(@NonNull String title, @NonNull UUID id);

  List<TaskCategory> findByUser_Id(@NonNull UUID id);
}