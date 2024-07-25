package com.example.task_list.service;

import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.task.TaskImage;

import java.util.List;

public interface TaskService {

    Task getById(Long taskId);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long taskId);

    void uploadImage(Long id, TaskImage image);
}
