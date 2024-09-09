package com.avab.avab.feign.kakao.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.avab.avab.feign.kakao.KaKaoOAuthFeignConfig;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthProfile;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthToken;

@FeignClient(name = "kakao-oauth-client", configuration = KaKaoOAuthFeignConfig.class)
public interface KakaoOAuthClient {
    @PostMapping("https://kauth.kakao.com/oauth/token")
    KakaoOAuthToken requestToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code);

    @PostMapping("https://kapi.kakao.com/v2/user/me")
    KakaoOAuthProfile requestKakaoProfile(@RequestHeader("Authorization") String token);
}
