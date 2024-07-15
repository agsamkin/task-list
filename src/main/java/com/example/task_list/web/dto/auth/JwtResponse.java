package com.example.task_list.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {

    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;

}
