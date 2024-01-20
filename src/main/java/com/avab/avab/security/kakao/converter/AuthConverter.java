package com.avab.avab.security.kakao.converter;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.security.kakao.dto.KakaoProfile;
import com.avab.avab.security.kakao.dto.LoginResponse;

public class AuthConverter {

    public static User toKakaoUser(KakaoProfile kakaoProfile) {
        return User.builder()
                .username(kakaoProfile.properties.nickname)
                .name(kakaoProfile.getKakao_account().name)
                .email(kakaoProfile.getKakao_account().email)
                .socialType(SocialType.KAKAO)
                .build();
    }

    public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return LoginResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }
}
