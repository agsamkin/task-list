package com.example.task_list.web.controller;

import com.example.task_list.domain.user.User;

import com.example.task_list.service.AuthService;
import com.example.task_list.service.UserService;
import com.example.task_list.web.dto.auth.JwtRequest;
import com.example.task_list.web.dto.auth.JwtResponse;
import com.example.task_list.web.dto.user.UserDto;
import com.example.task_list.web.dto.validation.OnCreate;

import com.example.task_list.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }

}