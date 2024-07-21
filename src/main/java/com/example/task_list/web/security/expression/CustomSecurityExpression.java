package com.example.task_list.web.security.expression;

import com.example.task_list.domain.user.Role;
import com.example.task_list.domain.user.User;
import com.example.task_list.service.AuthService;
import com.example.task_list.service.UserService;
import com.example.task_list.web.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(final Long userId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        SecurityUser user = getPrincipal();
        Long id = user.getId();

        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority
                    = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAccessTask(final Long taskId) {
        SecurityUser user = getPrincipal();
        Long id = user.getId();

        return userService.isTaskOwner(id, taskId);
    }

    private SecurityUser getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
    }

}
