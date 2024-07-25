package com.example.task_list.repository;

import com.example.task_list.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
        SELECT * FROM tasks t
        JOIN users_tasks ut
        ON t.id = ut.task_id
        WHERE ut.user_id = :userId
        """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

}
