package com.avab.avab.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth 🔐", description = "인증/인가 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행합니다. _by 준환_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/login/kakao")
    public BaseResponse<OAuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return BaseResponse.onSuccess(authService.kakaoLogin(code));
    }

    @Operation(
            summary = "JWT Access Token 재발급 API",
            description = "Refresh Token을 검증하고 새로운 Access Token과 Refresh Token을 응답합니다. _by 준환_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @PostMapping("/refresh")
    public BaseResponse<TokenRefreshResponse> refresh(@ExtractToken String refreshToken) {
        return BaseResponse.onSuccess(authService.refresh(refreshToken));
    }

    @Operation(summary = "로그아웃 API", description = "Refresh Token을 삭제합니다. _by 준환_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @DeleteMapping("/logout")
    public BaseResponse<String> logout(@Parameter(name = "user", hidden = true) @AuthUser User user) {
        authService.logout(user.getId());
        return BaseResponse.onSuccess("로그아웃에 성공하였습니다.");
    }
}
