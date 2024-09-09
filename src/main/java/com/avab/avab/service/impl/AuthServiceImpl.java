package com.avab.avab.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.converter.AuthConverter;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthProfile;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthToken;
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
    private final RandomUsername randomUsername;

    @Override
    @Transactional
    public OAuthResponse kakaoLogin(String code) {
        KakaoOAuthToken oAuthToken = kakaoAuthProvider.requestToken(code);
        KakaoOAuthProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccessToken());

        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmailAndSocialType(
                        kakaoProfile.getKakaoAccount().getEmail(), SocialType.KAKAO);

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            if (user.isDisabled()) {
                if (user.isCanBeEnabled()) {
                    user.enableUser();
                } else {
                    throw new AuthException(ErrorStatus.USER_DISABLED);
                }
            }
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, true, user);
        } else {
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
    public OAuthResponse kakaoLoginLocal(String code) {
        KakaoOAuthToken oAuthToken = kakaoAuthProvider.requestTokenLocal(code);
        KakaoOAuthProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccessToken());

        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmailAndSocialType(
                        kakaoProfile.getKakaoAccount().getEmail(), SocialType.KAKAO);

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            if (user.isDisabled()) {
                if (user.isCanBeEnabled()) {
                    user.enableUser();
                } else {
                    throw new AuthException(ErrorStatus.USER_DISABLED);
                }
            }
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, true, user);
        } else {
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

    @Override
    @Transactional
    public void enableUser(User user) {
        user.enableUser();
        userRepository.save(user);
    }
}
