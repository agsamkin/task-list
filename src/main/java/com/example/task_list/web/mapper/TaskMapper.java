package com.example.task_list.web.mapper;

import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.user.User;
import com.example.task_list.web.dto.task.TaskDto;
import com.example.task_list.web.dto.user.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task task);

    List<TaskDto> toDto(List<Task> tasks);

    Task toEntity(TaskDto dto);

}
