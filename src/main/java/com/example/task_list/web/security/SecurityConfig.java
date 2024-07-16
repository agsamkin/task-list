package com.example.task_list.web.security;

import com.example.task_list.web.security.jwt.JwtProperties;
import com.example.task_list.web.security.jwt.JwtTokenFilter;
import io.github.ilyalisov.jwt.service.TokenService;
import io.github.ilyalisov.jwt.service.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public TokenService tokenService() {
        return new TokenServiceImpl(jwtProperties.getSecret());
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity security) {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                .exceptionHandling(configure ->
                        configure.authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write("Unauthorized.");
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, authException) -> {
                                            response.setStatus(HttpStatus.FORBIDDEN.value());
                                            response.getWriter().write("Forbidden.");
                                        }
                                )
                )
                .authorizeHttpRequests(configure ->
                        configure.requestMatchers("/api/v*/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new JwtTokenFilter(
                                tokenService(),
                                userDetailsService
                        ), UsernamePasswordAuthenticationFilter.class
                );
        return security.build();
    }

}
