package com.example.task_list.web.controller;

import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.task.TaskImage;
import com.example.task_list.service.TaskService;
import com.example.task_list.web.dto.task.TaskDto;
import com.example.task_list.web.dto.task.TaskImageDto;
import com.example.task_list.web.dto.validation.OnUpdate;
import com.example.task_list.web.mapper.TaskImageMapper;
import com.example.task_list.web.mapper.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Task Controller",
        description = "Task API"
)
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @QueryMapping(name = "tasksById")
    @PreAuthorize("@cse.canAccessTask(#id)")
    @Operation(summary = "Get TaskDto by id")
    @GetMapping("/{id}")
    public TaskDto getById(
            @PathVariable @Argument(name = "id") final Long id
    ) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @MutationMapping(name = "updateTask")
    @PreAuthorize("@cse.canAccessTask(#dto.id)")
    @Operation(summary = "Update task")
    @PutMapping
    public TaskDto update(
            @Validated(OnUpdate.class)
            @RequestBody @Argument(name = "dto") final TaskDto dto
    ) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @MutationMapping(name = "deleteTask")
    @PreAuthorize("@cse.canAccessTask(#id)")
    @Operation(summary = "Delete task")
    @DeleteMapping("/{id}")
    public void deleteById(
            @PathVariable @Argument(name = "id") final Long id
    ) {
        taskService.delete(id);
    }

    @PreAuthorize("@cse.canAccessTask(#id)")
    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    public void uploadImage(
            @PathVariable final Long id,
            @Validated @ModelAttribute final TaskImageDto imageDto
    ) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }

}
