package com.avab.avab.auth.test;

import org.springframework.web.bind.annotation.*;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.auth.test.dto.LoginRequest;
import com.avab.avab.auth.test.dto.LoginResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return BaseResponse.onSuccess(authService.login(request.getEmail()));
    }
}
