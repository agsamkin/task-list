package com.example.task_list.service.impl;

import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.task.Status;
import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.task.TaskImage;
import com.example.task_list.repository.TaskRepository;
import com.example.task_list.service.ImageService;
import com.example.task_list.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ImageService imageService;

    @Cacheable(value = "TaskService::getById", key = "#taskId")
    @Transactional(readOnly = true)
    @Override
    public Task getById(final Long taskId) {
        return taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Task not found.")
                );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllByUserId(final Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Caching(put = {
            @CachePut(value = "TaskService::getById", key = "#task.id")
    })
    @Override
    public Task update(final Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Caching(cacheable = {
            @Cacheable(value = "TaskService::getById", key = "#task.id")
    })
    @Override
    public Task create(final Task task, final Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    @Override
    public void delete(final Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @CacheEvict(value = "TaskService::getById", key = "#id")
    @Override
    public void uploadImage(final Long id, final TaskImage image) {
        String fileName = imageService.uploadImage(image);
        taskRepository.addImage(id, fileName);
    }

    @Override
    public List<Task> getAllSoonTasks(
            final Duration duration
    ) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllSoonTasks(Timestamp.valueOf(now),
                Timestamp.valueOf(now.plus(duration)));
    }
}
