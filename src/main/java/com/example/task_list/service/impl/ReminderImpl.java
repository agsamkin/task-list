package com.example.task_list.service.impl;

import com.example.task_list.domain.MailType;
import com.example.task_list.domain.task.Task;
import com.example.task_list.domain.user.User;
import com.example.task_list.service.MailService;
import com.example.task_list.service.Reminder;
import com.example.task_list.service.TaskService;
import com.example.task_list.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    public static final Duration DURATION = Duration.ofHours(1);

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;

    //    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "0 * * * * *")
    @Override
    public void remindForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(DURATION);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }

}
