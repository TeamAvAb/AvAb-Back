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
    public LoginResponse login(String email, String password) {

        User user = userRepository.findByEmail(email);

        //        if ((!bCryptPasswordEncoder.matches(password, user.getPassword())) || user ==
        // null) {
        //            throw new AuthException(ErrorStatus._INVALID_LOGIN_REQUEST);
        //        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
