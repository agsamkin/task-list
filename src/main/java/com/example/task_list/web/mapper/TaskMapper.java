package com.example.task_list.web.mapper;

import com.example.task_list.domain.task.Task;
import com.example.task_list.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
