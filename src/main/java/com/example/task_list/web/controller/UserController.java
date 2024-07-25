package com.example.task_list.web.controller;

import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.user.User;
import com.example.task_list.service.TaskService;
import com.example.task_list.service.UserService;
import com.example.task_list.web.dto.task.TaskDto;
import com.example.task_list.web.dto.user.UserDto;
import com.example.task_list.web.dto.validation.OnCreate;
import com.example.task_list.web.dto.validation.OnUpdate;
import com.example.task_list.web.mapper.TaskMapper;
import com.example.task_list.web.mapper.UserMapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "User Controller",
        description = "User API"
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @QueryMapping(name = "userById")
    @PreAuthorize("@cse.canAccessUser(#id)")
    @Operation(summary = "Get UserDto by id")
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable @Argument(name = "id") final Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @MutationMapping(name = "updateUser")
    @PreAuthorize("@cse.canAccessUser(#dto.id)")
    @Operation(summary = "Update user")
    @PutMapping
    public UserDto update(
            @Validated(OnUpdate.class)
            @RequestBody @Argument(name = "dto") final UserDto dto
    ) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @MutationMapping(name = "deleteUser")
    @PreAuthorize("@cse.canAccessUser(#id)")
    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    public void deleteById(
            @PathVariable @Argument(name = "id") final Long id
    ) {
        userService.delete(id);
    }

    @QueryMapping(name = "tasksByUserId")
    @PreAuthorize("@cse.canAccessUser(#id)")
    @Operation(summary = "Get all User tasks")
    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(
            @PathVariable @Argument(name = "id") final Long id
    ) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @MutationMapping(name = "createTask")
    @PreAuthorize("@cse.canAccessUser(#id)")
    @Operation(summary = "Add task to user")
    @PostMapping("/{id}/tasks")
    public TaskDto createTask(
            @PathVariable @Argument(name = "id") final Long id,
            @Validated(OnCreate.class)
            @RequestBody @Argument(name = "dto") final TaskDto dto
    ) {
        Task task = taskMapper.toEntity(dto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }

}
