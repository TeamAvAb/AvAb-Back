package com.avab.avab.converter;

import com.avab.avab.domain.User;
import com.avab.avab.dto.response.UserResponseDTO;

public class UserConverter {

    public static UserResponseDTO.UserResponse convertUserToUserResponse(User user) {
        return UserResponseDTO.UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .socialType(user.getSocialType())
                .build();
    }
}
