package com.example.task_list.config;

import com.example.task_list.service.props.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@RequiredArgsConstructor
//@Configuration
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailSender.getHost());
        mailSender.setPort(mailSender.getPort());
        mailSender.setUsername(mailSender.getUsername());
        mailSender.setPassword(mailSender.getPassword());
        mailSender.setJavaMailProperties(mailProperties.getProperties());
        return mailSender;
    }

}
