package com.avab.avab.security.test;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.converter.AuthConverter;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.dto.response.AuthResponseDTO.OAuthResponse;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthProfile;
import com.avab.avab.feign.kakao.dto.response.KakaoOAuthResponseDTO.KakaoOAuthToken;
import com.avab.avab.redis.service.RefreshTokenService;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.provider.JwtTokenProvider;
import com.avab.avab.security.provider.KakaoAuthProvider;
import com.avab.avab.security.test.dto.LoginResponse;
import com.avab.avab.utils.RandomUsername;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestAuthService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RandomUsername randomUsername;

    @Transactional
    public LoginResponse login(String email) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());

        return LoginResponse.builder().accessToken(accessToken).build();
    }

    public OAuthResponse kakaoLoginLocal(String code) {
        KakaoOAuthToken oAuthToken = kakaoAuthProvider.requestTokenLocal(code);
        KakaoOAuthProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccessToken());

        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmailAndSocialType(
                        kakaoProfile.getKakaoAccount().getEmail(), SocialType.KAKAO);

        // 가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            if (user.isDeleted()) {
                return AuthConverter.toOAuthResponse(
                        jwtTokenProvider.createRestoreToken(user.getId()), null, true, user);
            }
            if (user.isDisabled()) {
                if (user.isCanBeEnabled()) {
                    user.enableUser();
                } else {
                    throw new AuthException(ErrorStatus.USER_DISABLED);
                }
            }
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, true, user);
        } else {
            User user =
                    userRepository.save(
                            AuthConverter.toUser(kakaoProfile, randomUsername.generate()));
            String accessToken = jwtTokenProvider.createAccessToken(user.getId());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenService.saveToken(refreshToken);
            return AuthConverter.toOAuthResponse(accessToken, refreshToken, false, user);
        }
    }
}
