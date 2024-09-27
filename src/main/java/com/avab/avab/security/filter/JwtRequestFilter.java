package com.avab.avab.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.principal.PrincipalDetails;
import com.avab.avab.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtTokenProvider.isTokenValid(token)) {
                Long userId = jwtTokenProvider.getId(token);
                User user =
                        userRepository
                                .findById(userId)
                                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
                UserDetails userDetails = new PrincipalDetails(user);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}
