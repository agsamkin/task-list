package com.example.task_list.service.impl;

import com.example.task_list.service.AuthService;
import com.example.task_list.web.dto.auth.JwtRequest;
import com.example.task_list.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }

}
