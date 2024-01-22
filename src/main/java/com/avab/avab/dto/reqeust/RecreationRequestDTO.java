package com.avab.avab.dto.reqeust;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class RecreationRequestDTO {

    @Getter
    @Setter
    public static class PostRecreationReviewDTO {

        @Min(value = 0, message = "별점은 0점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
        @NotNull(message = "별점은 필수입니다.")
        private Integer stars;

        @Size(max = 300, message = "리뷰는 300자 이하여야 합니다.")
        @NotEmpty(message = "리뷰 내용은 필수입니다.")
        private String contents;
    }
}
