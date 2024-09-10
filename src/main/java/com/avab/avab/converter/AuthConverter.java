package com.avab.avab.converter;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthProfile;

public class AuthConverter {

    public static User toUser(KakaoOAuthProfile kakaoProfile, String username) {
        return User.builder()
                .username(username)
                .name(kakaoProfile.getKakaoAccount().getName())
                .email(kakaoProfile.getKakaoAccount().getEmail())
                .socialType(SocialType.KAKAO)
                .build();
    }

    public static OAuthResponse toOAuthResponse(
            String accessToken, String refreshToken, Boolean isLogin, User user) {
        return OAuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .isLogin(isLogin)
                .userId(user.getId())
                .build();
    }

    public static TokenRefreshResponse toTokenRefreshResponse(
            String accessToken, String refreshToken) {
        return TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
