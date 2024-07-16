package com.example.task_list.web.security.jwt;

import io.github.ilyalisov.jwt.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @SneakyThrows
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) {

        try {
            String token = resolve((HttpServletRequest) servletRequest);
            if (!token.isEmpty()
                    && tokenService.getType(token).equals(TokenType.ACCESS)
                    && !tokenService.isExpired(token)) {

                Authentication authentication = getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception ignored) {
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolve(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("bearer ")) {
            return bearerToken.substring(7);
        }

        return "";
    }

    private Authentication getAuthentication(String token) {
        String subject = tokenService.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails, "", userDetails.getAuthorities()
            );
        }

        return null;
    }

}
