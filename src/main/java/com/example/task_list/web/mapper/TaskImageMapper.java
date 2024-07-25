package com.example.task_list.web.mapper;

import com.example.task_list.domain.task.TaskImage;
import com.example.task_list.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
