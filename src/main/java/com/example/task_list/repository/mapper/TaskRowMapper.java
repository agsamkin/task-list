package com.example.task_list.repository.mapper;

import com.example.task_list.domain.task.Status;
import com.example.task_list.domain.task.Task;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaskRowMapper {

    @SneakyThrows
    public static Task mapRow(ResultSet rs) {
        if (rs.next()) {
            Task task = new Task();
            task.setId(rs.getLong("task_id"));
            task.setTitle(rs.getString("task_title"));
            task.setDescription(rs.getString("task_description"));
            Timestamp timestamp = rs.getTimestamp("task_expiration_date");
            if (timestamp != null) {
                task.setExpirationDate(timestamp.toLocalDateTime());
            }
            task.setStatus(Status.valueOf(rs.getString("task_status")));
            return task;
        }
        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(ResultSet resultSet) {
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            if (!resultSet.wasNull()) {
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                task.setStatus(Status.valueOf(resultSet.getString("task_status")));
                Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
                if (timestamp != null) {
                    task.setExpirationDate(timestamp.toLocalDateTime());
                }
                tasks.add(task);
            }
        }
        return tasks;
    }

}
