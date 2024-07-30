package com.example.task_list.service;

import com.example.task_list.domain.MailType;
import com.example.task_list.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(
            User user,
            MailType type,
            Properties properties
    );

}
