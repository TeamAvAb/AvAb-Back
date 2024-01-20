package com.avab.avab.redis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.redis.domain.RefreshToken;
import com.avab.avab.redis.repository.RefreshTokenRepository;
import com.avab.avab.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public boolean isEqualsToken(String refreshToken) {
        RefreshToken savedrefreshToken =
                refreshTokenRepository
                        .findById(jwtTokenProvider.getId(refreshToken))
                        .orElseThrow(
                                () -> {
                                    throw new AuthException(ErrorStatus.NOT_CONTAIN_TOKEN);
                                });
        if (!savedrefreshToken.getToken().equals(refreshToken)) {
            return false;
        }

        return true;
    }

    @Transactional
    public void saveToken(String refreshToken) {
        RefreshToken newRefreshToken =
                RefreshToken.builder()
                        .id(jwtTokenProvider.getId(refreshToken))
                        .token(refreshToken)
                        .build();
        refreshTokenRepository.save(newRefreshToken);
    }
}
