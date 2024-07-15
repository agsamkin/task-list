package com.example.task_list.service;

import com.example.task_list.domain.user.User;

public interface UserService {

    User getById(Long userId);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    void delete(Long userId);

}
