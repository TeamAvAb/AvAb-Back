package com.avab.avab.auth.test;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.avab.avab.apiPayload.ApiResponse;
import com.avab.avab.auth.jwt.JwtTokenProvider;
import com.avab.avab.auth.test.dto.LoginRequest;
import com.avab.avab.auth.test.dto.LoginResponse;
import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.onSuccess(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody LoginRequest request) {
        User user = User.builder().email(request.getEmail()).build();

        User user1 = userRepository.save(user);
        return ResponseEntity.ok(user1.getId());
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam String token) {
        return ResponseEntity.ok(jwtTokenProvider.getId(token));
    }
}
