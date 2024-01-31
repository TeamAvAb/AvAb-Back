package com.avab.avab.dto.response;

import com.avab.avab.domain.enums.SocialType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserResponse {
        private Long id;

        private String email;

        private String name;

        private String username;

        private SocialType socialType;
    }
}
