package com.example.task_list.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Request after login")
@Data
public class JwtResponse {

    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;

}
