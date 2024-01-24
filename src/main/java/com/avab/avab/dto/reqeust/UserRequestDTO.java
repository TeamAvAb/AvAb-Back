package com.avab.avab.dto.reqeust;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class UpdateUserNameDTO {
        private String name;
    }
}
