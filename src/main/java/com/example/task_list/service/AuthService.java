package com.example.task_list.service;

import com.example.task_list.web.dto.auth.JwtRequest;
import com.example.task_list.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
