package com.example.task_list.service.impl;

import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.user.Role;
import com.example.task_list.domain.user.User;
import com.example.task_list.repository.UserRepository;
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

import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Lazy))
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Cacheable(value = "UserService::getById", key = "#userId")
    @Transactional(readOnly = true)
    @Override
    public User getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Cacheable(value = "UserService::getByUsername", key = "#username")
    @Transactional(readOnly = true)
    @Override
    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Caching(put = {
                @CachePut(value = "UserService::getById", key = "#user.id"),
                @CachePut(value = "UserService::getByUsername", key = "#user.username")
    })
    @Override
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.id"),
            @Cacheable(value = "UserService::getByUsername", key = "#user.username")
    })
    @Override
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Cacheable(value = "UserService::isTaskOwner", key = "#userId" + "." + "#taskId")
    @Transactional(readOnly = true)
    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @CacheEvict(value = "UserService::getById", key = "#userId")
    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

}
