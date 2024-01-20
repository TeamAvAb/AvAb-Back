package com.avab.avab.converter;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.dto.KakaoProfile;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;

public class AuthConverter {

    public static User toUser(KakaoProfile kakaoProfile) {
        return User.builder()
                   .username(kakaoProfile.getProperties().getNickname())
                   .name(kakaoProfile.getKakao_account().getName())
                   .email(kakaoProfile.getKakao_account().getEmail())
                   .socialType(SocialType.KAKAO)
                   .build();
    }

    public static OAuthResponse toOAuthResponse(String accessToken, String refreshToken, Boolean isLogin, User user) {
        return OAuthResponse.builder().refreshToken(refreshToken).accessToken(accessToken).isLogin(isLogin).userId(user.getId()).build();
    }

    public static TokenRefreshResponse toTokenRefreshResponse(String accessToken, String refreshToken) {
        return TokenRefreshResponse.builder()
                                   .accessToken(accessToken)
                                   .refreshToken(refreshToken)
                                   .build();
    }
}
