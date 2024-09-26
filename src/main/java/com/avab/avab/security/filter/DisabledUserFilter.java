package com.avab.avab.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.domain.User;
import com.avab.avab.service.AuthService;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DisabledUserFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AuthService authService;
    private final List<Pair<HttpMethod, Pattern>> notPermittedRequests =
            List.of(
                    Pair.of(HttpMethod.POST, Pattern.compile("/api/flows")),
                    Pair.of(HttpMethod.PATCH, Pattern.compile("/api/flows/\\d+")),
                    Pair.of(HttpMethod.POST, Pattern.compile("/api/recreations")),
                    Pair.of(HttpMethod.POST, Pattern.compile("/api/recreations/\\d+/reviews")),
                    Pair.of(
                            HttpMethod.POST,
                            Pattern.compile("/api/recreation-reviews/\\d+/recommendations")));

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;
        if (authentication != null) {
            principal = authentication.getPrincipal();
        }
        if (principal != null && principal.getClass() != String.class) {

            UsernamePasswordAuthenticationToken authenticationToken =
                    (UsernamePasswordAuthenticationToken) authentication;
            Long userId = Long.valueOf(authenticationToken.getName());

            User user = userService.findUserById(userId);
            if (user.isDisabled()) {
                if (user.isCanBeEnabled()) {
                    authService.enableUser(user);
                } else {
                    throw new AuthException(ErrorStatus.USER_DISABLED);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isPermittedRequest(request);
    }

    private Boolean isPermittedRequest(HttpServletRequest request) {
        return notPermittedRequests.stream()
                .noneMatch(
                        pair -> {
                            String method = pair.getFirst().name();
                            Pattern urlMatcher = pair.getSecond();
                            return method.equals(request.getMethod())
                                    && urlMatcher.matcher(request.getRequestURI()).matches();
                        });
    }
}
