package com.oasis.backend.repositories;

import com.oasis.backend.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
        select t from Task t where upper(t.category.title) = upper(?1) and t.user.id = ?2
        group by t.id, t.priority
        order by case t.priority
        when com.oasis.backend.enums.TaskPriority.HIGH then 0
        WHEN com.oasis.backend.enums.TaskPriority.MEDIUM THEN 1
        ELSE 2 END
    """)
    Page<Task> findByCategoryTitleIgnoreCase(@NonNull String title, @NonNull UUID id, Pageable pageable);

    @Query(
            value = """
                SELECT tks.* FROM public.tasks tks
                LEFT JOIN public.task_categories tksC ON tks.category_id = tksC.id
                LEFT JOIN public.users u ON tks.user_id = u.id
                WHERE
                (to_tsvector('english', tks.title) @@ to_tsquery(:query)
                OR to_tsvector('english', tks.description) @@ to_tsquery(:query))
                and u.id = :userId and upper(tksC.title) = upper(:category)
                GROUP BY tks.id, tks.title, tks.description, tks.priority
                ORDER BY
                CASE tks.priority
                WHEN 'HIGH' THEN 0
                WHEN 'MEDIUM' THEN 1
                ELSE 2 END,
                GREATEST(ts_rank_cd(to_tsvector('english', tks.title), to_tsquery(:query)),
                ts_rank_cd(to_tsvector('english', tks.description), to_tsquery(:query))) DESC
            """,
            nativeQuery = true
    )
    Page<Task> fullTextSearch(
            @Param("query") String query,
            @Param("userId") UUID userId,
            @Param("category") String category,
            Pageable pageable
    );
}