package com.example.task_list.service.impl;

import com.example.task_list.domain.MailType;
import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.user.Role;
import com.example.task_list.domain.user.User;
import com.example.task_list.repository.UserRepository;
import com.example.task_list.service.MailService;
import com.example.task_list.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Lazy))
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Cacheable(value = "UserService::getById", key = "#userId")
    @Transactional(readOnly = true)
    @Override
    public User getById(final Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );
    }

    @Cacheable(value = "UserService::getByUsername", key = "#username")
    @Transactional(readOnly = true)
    @Override
    public User getByUsername(final String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found.")
                );
    }

    @Caching(put = {
            @CachePut(
                    value = "UserService::getById",
                    key = "#user.id"),
            @CachePut(
                    value = "UserService::getByUsername",
                    key = "#user.username")})
    @Override
    public User update(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Caching(cacheable = {
            @Cacheable(
                    value = "UserService::getById",
                    condition = "#user.id!=null",
                    key = "#user.id"
            ),
            @Cacheable(
                    value = "UserService::getByUsername",
                    condition = "#user.username!=null",
                    key = "#user.username"
            )
    })
    @Override
    public User create(final User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException(
                    "Password and password confirmation do not match."
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirmation(passwordEncoder.encode(user.getPasswordConfirmation()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);

        mailService.sendEmail(user, MailType.REGISTRATION, new Properties());

        return user;
    }

    @Cacheable(
            value = "UserService::isTaskOwner",
            key = "#userId" + "." + "#taskId"
    )
    @Transactional(readOnly = true)
    @Override
    public boolean isTaskOwner(final Long userId, final Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Cacheable(
            value = "UserService::getTaskAuthor",
            key = "#taskId"
    )
    public User getTaskAuthor(
            final Long taskId
    ) {
        return userRepository.findTaskAuthor(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    @CacheEvict(
            value = "UserService::getById",
            key = "#userId"
    )
    @Override
    public void delete(final Long userId) {
        userRepository.deleteById(userId);
    }

}
