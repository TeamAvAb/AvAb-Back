package com.avab.avab.service;

import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.dto.response.AuthResponseDTO.TokenRefreshResponse;

public interface AuthService {

    OAuthResponse kakaoLogin(String code);

    TokenRefreshResponse refresh(String refreshToken);

    void logout(Long userId);
}
