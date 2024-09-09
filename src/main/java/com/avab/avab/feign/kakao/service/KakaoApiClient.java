package com.avab.avab.feign.kakao.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.avab.avab.feign.kakao.KaKaoOAuthFeignConfig;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO;

@FeignClient(
        name = "kakao-api-client",
        url = "https://kapi.kakao.com",
        configuration = KaKaoOAuthFeignConfig.class)
public interface KakaoApiClient {

    @PostMapping("/v2/user/me")
    KakaoOAuthResponseDTO.KakaoOAuthProfile requestKakaoProfile(
            @RequestHeader("Authorization") String token);
}
