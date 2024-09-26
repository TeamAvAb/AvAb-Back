package com.avab.avab.security.test;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.security.test.dto.LoginRequest;
import com.avab.avab.security.test.dto.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Test Auth 🧪🔐", description = "인증/인가 테스트 용")
@Profile({"local", "dev"})
public class TestAuthController {

    private final TestAuthService authService;

    @GetMapping("/login/test")
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        return BaseResponse.onSuccess(authService.login(request.getEmail()));
    }

    @Operation(summary = "카카오 로그인 로컬 테스트 API", description = "리다이렉트 URL이 로컬호스트인 카카오 로그인 _by 보노_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/login/kakao/local")
    public BaseResponse<OAuthResponse> kakaoLoginLocal(@RequestParam("code") String code) {
        return BaseResponse.onSuccess(authService.kakaoLoginLocal(code));
    }
}
