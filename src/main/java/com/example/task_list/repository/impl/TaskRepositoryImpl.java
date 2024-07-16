package com.example.task_list.repository.impl;

import com.example.task_list.domain.task.Task;
import com.example.task_list.repository.DataSourceConfig;
import com.example.task_list.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;


    @Override
    public Optional<Task> findById(Long taskId) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public void assignToUserId(Long taskId, Long userId) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void create(Task task) {

    }

    @Override
    public void delete(Long taskId) {

    }

}
