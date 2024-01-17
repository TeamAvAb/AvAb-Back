package com.avab.avab.security.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.provider.JwtTokenProvider;
import com.avab.avab.security.test.dto.LoginResponse;

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
