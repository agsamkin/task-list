package com.example.task_list.service.impl;

import com.example.task_list.domain.user.User;
import com.example.task_list.service.AuthService;
import com.example.task_list.service.UserService;
import com.example.task_list.web.dto.auth.JwtRequest;
import com.example.task_list.web.dto.auth.JwtResponse;

import com.example.task_list.web.security.jwt.JwtProperties;
import com.example.task_list.web.security.jwt.TokenType;

import io.github.ilyalisov.jwt.config.TokenParameters;
import io.github.ilyalisov.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    @Override
    public JwtResponse login(final JwtRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.getByUsername(request.getUsername());

        JwtResponse response = new JwtResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());

        response.setAccessToken(
                tokenService.create(
                        TokenParameters.builder(
                                        request.getUsername(),
                                        TokenType.ACCESS.name(),
                                        jwtProperties.getAccessDuration()
                                )
                                .build()
                )
        );

        response.setRefreshToken(
                tokenService.create(
                        TokenParameters.builder(
                                        request.getUsername(),
                                        TokenType.REFRESH.name(),
                                        jwtProperties.getRefreshDuration()
                                )
                                .build()
                )
        );

        return response;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (refreshToken.isEmpty()
                || !tokenService.getType(refreshToken).equals(TokenType.REFRESH.name())
                || tokenService.isExpired(refreshToken)) {
            return null;
        }

        String subject = tokenService.getSubject(refreshToken);
        User user = userService.getByUsername(subject);

        JwtResponse response = new JwtResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());

        response.setAccessToken(
                tokenService.create(
                        TokenParameters.builder(
                                        user.getUsername(),
                                        TokenType.ACCESS.name(),
                                        jwtProperties.getAccessDuration()
                                )
                                .build()
                )
        );

        response.setRefreshToken(
                tokenService.create(
                        TokenParameters.builder(
                                        user.getUsername(),
                                        TokenType.REFRESH.name(),
                                        jwtProperties.getRefreshDuration()
                                )
                                .build()
                )
        );

        return response;
    }

}
