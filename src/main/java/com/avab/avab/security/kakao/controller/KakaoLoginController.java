package com.avab.avab.security.kakao.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.security.kakao.dto.LoginResponse;
import com.avab.avab.security.kakao.dto.RefreshRequest;
import com.avab.avab.security.kakao.service.KakaoLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/auth/kakao/callback")
    public BaseResponse<LoginResponse> kakaoLogin(@RequestParam("code") String code) {
        return BaseResponse.onSuccess(kakaoLoginService.kakaoLogin(code));
    }

    @PostMapping("/auth/refresh")
    public BaseResponse<LoginResponse> refresh(@Valid @RequestBody RefreshRequest refreshRequest) {
        return BaseResponse.onSuccess(kakaoLoginService.refresh(refreshRequest.getRefreshToken()));
    }
}
