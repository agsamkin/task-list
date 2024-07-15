package com.example.task_list.service.impl;

import com.example.task_list.domain.task.Task;
import com.example.task_list.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public Task getById(Long taskId) {
        return null;
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public Task create(Task task, Long userId) {
        return null;
    }

    @Override
    public void delete(Long taskId) {

    }

}
