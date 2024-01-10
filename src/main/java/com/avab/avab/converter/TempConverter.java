package com.avab.avab.converter;

import com.avab.avab.DTO.TempResponse;

public class TempConverter {

    public static TempResponse.TempTestDTO toTempTestDTO() {
        return TempResponse.TempTestDTO.builder().testString("AVAB 아자아자!").build();
    }

    public static TempResponse.TempExceptionDTO toTempExceptionDTO(Integer flag) {
        return TempResponse.TempExceptionDTO.builder().flag(flag).build();
    }
}
