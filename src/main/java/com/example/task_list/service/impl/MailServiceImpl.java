package com.example.task_list.service.impl;

import com.example.task_list.domain.MailType;
import com.example.task_list.domain.user.User;
import com.example.task_list.service.MailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(
            User user,
            MailType type,
            Properties properties
    ) {

        switch (type) {
            case REGISTRATION -> sendRegistrationMail(user, properties);
            case REMINDER -> sendReminderMail(user, properties);
            default -> {}
        }

    }

    @SneakyThrows
    private void sendRegistrationMail(User user, Properties properties) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, false, "UTF-8");
        helper.setSubject("Thanks you for registration " + user.getName());
        helper.setTo(user.getUsername());
        String text = getRegistrationEmailContent(user, properties);
        helper.setText(text, true);

        mailSender.send(mimeMessage);

    }

    @SneakyThrows
    private void sendReminderMail(User user, Properties properties) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage, false, "UTF-8");
        helper.setSubject("You have task to do in 1 hour");
        helper.setTo(user.getUsername());
        String text = getReminderEmailContent(user, properties);
        helper.setText(text, true);

        mailSender.send(mimeMessage);

    }

    @SneakyThrows
    private String getRegistrationEmailContent(
            final User user,
            final Properties properties
    ) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getReminderEmailContent(
            final User user,
            final Properties properties
    ) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

}
