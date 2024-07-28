package com.example.task_list.service.impl;

import com.example.task_list.config.TestConfig;
import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.task.Status;
import com.example.task_list.domain.task.Task;

import com.example.task_list.domain.task.TaskImage;
import com.example.task_list.repository.TaskRepository;
import com.example.task_list.repository.UserRepository;
import com.example.task_list.service.ImageService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        Task testTask = taskService.getById(taskId);

        Mockito.verify(taskRepository).findById(taskId);
        assertThat(testTask)
                .usingRecursiveComparison()
                .isEqualTo(task);
    }

    @Test
    void getByIdWithNotExistingId() {
        Long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getById(taskId)
        );
        Mockito.verify(taskRepository).findById(taskId);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);

        List<Task> testTasks = taskService.getAllByUserId(userId);

        Mockito.verify(taskRepository).findAllByUserId(userId);
        assertThat(testTasks)
                .usingRecursiveComparison()
                .isEqualTo(tasks);
    }

    @Test
    void update() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");
        task.setExpirationDate(LocalDateTime.now());
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        Task testTask = taskService.update(task);

        Mockito.verify(taskRepository).save(task);
        assertThat(testTask)
                .usingRecursiveComparison()
                .isEqualTo(task);
    }

    @Test
    void updateWithEmptyStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        Task testTask = taskService.update(task);

        Mockito.verify(taskRepository).save(task);
        assertThat(testTask)
                .usingRecursiveComparison()
                .ignoringFields("status")
                .isEqualTo(task);
        assertThat(testTask.getStatus()).isEqualTo(Status.TODO);
    }

    @Test
    void create() {
        Long userId = 1L;
        Long taskId = 1L;
        Task task = new Task();
        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).save(task);

        Task testTask = taskService.create(task, userId);

        Mockito.verify(taskRepository).save(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository)
                .assignTask(userId, task.getId());
    }

    @Test
    void delete() {
        Long id = 1L;

        taskService.delete(id);

        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.uploadImage(taskImage))
                .thenReturn(imageName);

        taskService.uploadImage(id, taskImage);

        Mockito.verify(taskRepository).addImage(id, imageName);
    }

}
