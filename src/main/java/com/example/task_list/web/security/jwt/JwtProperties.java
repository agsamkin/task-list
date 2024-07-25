package com.example.task_list.web.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;

    //    @Value("${access-duration}")
    private Duration accessDuration;

    //    @Value("${refresh-duration}")
    private Duration refreshDuration;

}
