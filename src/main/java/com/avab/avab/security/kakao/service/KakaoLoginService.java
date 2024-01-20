package com.avab.avab.security.kakao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.domain.User;
import com.avab.avab.redis.service.RefreshTokenService;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.kakao.KakaoAuthProvider;
import com.avab.avab.security.kakao.converter.AuthConverter;
import com.avab.avab.security.kakao.dto.KakaoProfile;
import com.avab.avab.security.kakao.dto.LoginResponse;
import com.avab.avab.security.kakao.dto.OAuthToken;
import com.avab.avab.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse kakaoLogin(String code) {
        OAuthToken oAuthToken = kakaoAuthProvider.requestToken(code);
        KakaoProfile kakaoProfile = kakaoAuthProvider.requestKakao(oAuthToken.getAccess_token());
        User kakaoUser = AuthConverter.toKakaoUser(kakaoProfile);

        // 유저 정보 받기
        User originUser = userRepository.findByEmail(kakaoUser.getEmail());

        LoginResponse loginResponse = null;

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (originUser == null) {
            loginResponse = kakaoAuthProvider.signUp(kakaoUser);
        } else {
            loginResponse = kakaoAuthProvider.login(originUser);
        }

        return loginResponse;
    }

    @Transactional
    public LoginResponse refresh(String refreshToken) {
        LoginResponse loginResponse = null;

        if (jwtTokenProvider.isTokenValid(refreshToken)) {
            if (refreshTokenService.isEqualsToken(refreshToken)) {
                String accessToken =
                        jwtTokenProvider.createAccessToken(jwtTokenProvider.getId(refreshToken));
                String newRefreshToken =
                        jwtTokenProvider.createAccessToken(jwtTokenProvider.getId(refreshToken));
                refreshTokenService.saveToken(newRefreshToken);
                loginResponse = AuthConverter.toLoginResponse(accessToken, newRefreshToken);
            } else {
                throw new AuthException(ErrorStatus.NOT_EQUAL_TOKEN);
            }
        } else {
            throw new AuthException(ErrorStatus.AUTH_EXPIRED_TOKEN);
        }

        return loginResponse;
    }
}
