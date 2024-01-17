package com.avab.avab.security.test;

import org.springframework.web.bind.annotation.*;

import com.avab.avab.apiPayload.ApiResponse;
import com.avab.avab.security.test.dto.LoginRequest;
import com.avab.avab.security.test.dto.LoginResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.onSuccess(authService.login(request.getEmail()));
    }
}
