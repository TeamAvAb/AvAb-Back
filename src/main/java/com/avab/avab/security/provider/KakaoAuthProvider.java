package com.avab.avab.security.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthProfile;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthToken;
import com.avab.avab.feign.kakao.service.KakaoApiClient;
import com.avab.avab.feign.kakao.service.KakaoOAuthClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider {
    private final KakaoOAuthClient kakaoOAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${kakao.auth.client}")
    private String client;

    @Value("${kakao.auth.redirect-uri}")
    private String redirect;

    // code로 access 토큰 요청하기
    public KakaoOAuthToken requestToken(String code) {
        return kakaoOAuthClient.requestToken("authorization_code", client, redirect, code);
    }

    public KakaoOAuthToken requestTokenLocal(String code) {
        return kakaoOAuthClient.requestToken(
                "authorization_code",
                client,
                "http://localhost:3000/AvAb-Front/api/auth/login/kakao",
                code);
    }

    // Token으로 정보 요청하기
    public KakaoOAuthProfile requestKakaoProfile(String token) {
        return kakaoApiClient.requestKakaoProfile("Bearer " + token);
    }
}
