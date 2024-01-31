package com.avab.avab.controller;

import org.springframework.web.bind.annotation.*;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.domain.User;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.security.handler.annotation.ExtractToken;
import com.avab.avab.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행하는 API입니다. by 준환")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/login/kakao")
    public BaseResponse<OAuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return BaseResponse.onSuccess(authService.kakaoLogin(code));
    }

    @Operation(
            summary = "Refresh Token 검증 API",
            description = "Refresh Token을 검증하고 새로운 Access Token과 Refresh Token을 돌려주는 API입니다. by 준환")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/refresh")
    public BaseResponse<TokenRefreshResponse> refresh(@ExtractToken String refreshToken) {
        return BaseResponse.onSuccess(authService.refresh(refreshToken));
    }

    @Operation(summary = "로그아웃 API", description = "REDIS의 refresh 토큰을 없애주는 API입니다. by 준환")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @DeleteMapping("/logout")
    public BaseResponse<String> logout(@AuthUser User user) {
        authService.logout(user.getId());
        return BaseResponse.onSuccess("로그아웃에 성공하였습니다.");
    }
}
