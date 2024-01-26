package com.avab.avab.dto.reqeust;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class UpdateUserNameDTO {

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;
    }
}
