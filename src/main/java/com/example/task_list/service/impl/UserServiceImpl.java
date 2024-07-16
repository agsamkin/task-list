package com.example.task_list.service.impl;

import com.example.task_list.domain.exception.ResourceNotFoundException;
import com.example.task_list.domain.user.Role;
import com.example.task_list.domain.user.User;
import com.example.task_list.repository.UserRepository;
import com.example.task_list.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public User getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));;
    }

    @Override
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    @Override
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.create(user);
        userRepository.insertUserRole(user.getId(), Role.ROLE_USER);
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }

}
