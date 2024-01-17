package com.avab.avab.security.handler.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.GeneralException;
import com.avab.avab.domain.User;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        System.out.println("hihi");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;
        if (authentication != null) {
            // 로그인하지 않은 익명 사용자라면 null 반환
            if (authentication.getName().equals("anonymousUser")) {
                return null;
            }
            principal = authentication.getPrincipal();
        }
        if (principal == null || principal.getClass() == String.class) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) authentication;
        Long userId = Long.valueOf(authenticationToken.getName());

        return userService.findUserById(userId);
    }
}
