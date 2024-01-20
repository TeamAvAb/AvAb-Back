package com.avab.avab.security.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.domain.User;
import com.avab.avab.redis.service.RefreshTokenService;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.kakao.dto.KakaoProfile;
import com.avab.avab.security.kakao.dto.LoginResponse;
import com.avab.avab.security.kakao.dto.OAuthToken;
import com.avab.avab.security.principal.PrincipalDetailsService;
import com.avab.avab.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.auth.client}")
    private String client;

    @Value("${kakao.auth.redirect}")
    private String redirect;

    // code로 access 토큰 요청하기
    public OAuthToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kauth.kakao.com/oauth/token",
                        HttpMethod.POST,
                        kakaoTokenRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO);
        }

        return oAuthToken;
    }

    // Token으로 정보 요청하기
    public KakaoProfile requestKakao(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.POST,
                        kakaoProfileRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new AuthException(ErrorStatus.INVALID_REQUEST_INFO);
        }

        return kakaoProfile;
    }

    // DB에 관련 유저가 없으면 회원가입
    @Transactional
    public LoginResponse signUp(User kakaoUser) {
        User user = userRepository.save(kakaoUser);
        sign(user);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        refreshTokenService.saveToken(refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    // 있으면 로그인
    @Transactional(readOnly = true)
    public LoginResponse login(User originUser) {
        sign(originUser);
        String accessToken = jwtTokenProvider.createAccessToken(originUser.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(originUser.getId());
        refreshTokenService.saveToken(refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    // 권한 부여 로직
    private void sign(User user) {
        UserDetails userDetails =
                principalDetailsService.loadUserByUsername(user.getId().toString());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
