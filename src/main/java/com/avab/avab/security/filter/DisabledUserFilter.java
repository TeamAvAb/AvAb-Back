package com.avab.avab.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.domain.User;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DisabledUserFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName() + "hello");

        Object principal = null;
        if (authentication != null) {
            principal = authentication.getPrincipal();
        }
        if (principal != null && principal.getClass() != String.class) {

            UsernamePasswordAuthenticationToken authenticationToken =
                    (UsernamePasswordAuthenticationToken) authentication;
            Long userId = Long.valueOf(authenticationToken.getName());

            User user = userService.findUserById(userId);
            if (user.getDisabledAt().plusDays(7).isAfter(LocalDateTime.now())) {
                log.debug("User is disabled: {}", user.getEmail());
                throw new AuthException(ErrorStatus.USER_DISABLED);
            } else {
                log.debug("enable user: {}", user.getEmail());
                userService.enableUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
