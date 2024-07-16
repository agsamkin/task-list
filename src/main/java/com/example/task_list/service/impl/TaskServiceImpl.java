package com.example.task_list.service.impl;

import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.task.Status;
import com.example.task_list.domain.task.Task;
import com.example.task_list.repository.TaskRepository;
import com.example.task_list.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public Task getById(Long taskId) {
        return taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);
        return task;
    }

    @Override
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.create(task);
        taskRepository.assignToUserId(task.getId(), userId);
        return task;
    }

    @Override
    public void delete(Long taskId) {
        taskRepository.delete(taskId);
    }

}
