package com.example.task_list.config;

import com.example.task_list.repository.TaskRepository;
import com.example.task_list.repository.UserRepository;
import com.example.task_list.service.AuthService;
import com.example.task_list.service.ImageService;
import com.example.task_list.service.TaskService;
import com.example.task_list.service.UserService;
import com.example.task_list.service.impl.AuthServiceImpl;
import com.example.task_list.service.impl.ImageServiceImpl;
import com.example.task_list.service.impl.TaskServiceImpl;
import com.example.task_list.service.impl.UserServiceImpl;
import com.example.task_list.service.props.MinioProperties;
import com.example.task_list.web.security.SecurityUserService;
import com.example.task_list.web.security.jwt.JwtProperties;
import io.github.ilyalisov.jwt.service.TokenService;
import io.github.ilyalisov.jwt.service.TokenServiceImpl;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@TestConfiguration
public class TestConfig {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AuthenticationManager authenticationManager;

    @Bean
    @Primary
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "dmpraGFqZjV0ZWd3ZWllamRranJlNGhmcG93aw=="
        );
        return jwtProperties;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new SecurityUserService(userService());
    }

    @Bean
    public MinioClient minioClient() {
        return new Mockito().mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties minioProperties = new MinioProperties();
        minioProperties.setBucket("images");
        return minioProperties;
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(
                minioClient(),
                minioProperties()
        );
    }

    @Bean
    @Primary
    public UserServiceImpl userService() {
        return new UserServiceImpl(passwordEncoder(), userRepository);
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService() {
        return new TaskServiceImpl(
                taskRepository,
                imageService()
        );
    }


    @Bean
    public TokenService tokenService() {
        return new TokenServiceImpl(jwtProperties().getSecret());
    }

    @Bean
    @Primary
    public AuthServiceImpl authService() {
        return new AuthServiceImpl(
                authenticationManager,
                userService(),
                tokenService(),
                jwtProperties()
        );
    }

}
