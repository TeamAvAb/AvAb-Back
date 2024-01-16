package com.avab.avab.auth.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.auth.jwt.JwtTokenProvider;
import com.avab.avab.auth.test.dto.LoginResponse;
import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(String email) {

        User user = userRepository.findByEmail(email);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());

        return LoginResponse.builder().accessToken(accessToken).build();
    }
}
