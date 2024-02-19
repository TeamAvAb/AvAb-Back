package com.avab.avab.dto.reqeust;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class UpdateUserDTO {

        @NotBlank(message = "닉네임을 입력해주세요.")
        private String username;
    }
}
