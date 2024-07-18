package com.example.task_list.web.security;

import com.example.task_list.domain.user.User;
import com.example.task_list.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserService userService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        User client = userService.getByUsername(username);
        return new SecurityUser(client);
    }

}
