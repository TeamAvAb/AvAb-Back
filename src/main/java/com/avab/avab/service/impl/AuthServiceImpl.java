package com.avab.avab.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.converter.AuthConverter;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.dto.KakaoProfile;
import com.avab.avab.dto.OAuthToken;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;
import com.avab.avab.redis.service.RefreshTokenService;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.provider.JwtTokenProvider;
import com.avab.avab.security.provider.KakaoAuthProvider;
import com.avab.avab.service.AuthService;
import com.avab.avab.utils.RandomUsername;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public OAuthResponse kakaoLogin(String code) {
        OAuthToken oAuthToken = kakaoAuthProvider.requestToken(code);
        KakaoProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());

        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmailAndSocialType(
                        kakaoProfile.getKakao_account().getEmail(), SocialType.KAKAO);

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, true, user);
        } else {
            RandomUsername randomUsername = new RandomUsername();
            User user =
                    userRepository.save(
                            AuthConverter.toUser(kakaoProfile, randomUsername.generate()));
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, false, user);
        }
    }

    @Override
    @Transactional
    public TokenRefreshResponse refresh(String refreshToken) {
        jwtTokenProvider.isTokenValid(refreshToken);

        if (!refreshTokenService.isEqualsToken(refreshToken)) {
            throw new AuthException(ErrorStatus.NOT_EQUAL_TOKEN);
        }

        String accessToken =
                jwtTokenProvider.createAccessToken(jwtTokenProvider.getId(refreshToken));
        String newRefreshToken =
                jwtTokenProvider.createAccessToken(jwtTokenProvider.getId(refreshToken));
        refreshTokenService.saveToken(newRefreshToken);
        return AuthConverter.toTokenRefreshResponse(accessToken, newRefreshToken);
    }

    public void logout(Long userId) {
        refreshTokenService.deleteToken(userId);
    }
}
