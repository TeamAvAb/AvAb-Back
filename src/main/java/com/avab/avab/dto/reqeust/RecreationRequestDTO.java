package com.avab.avab.dto.reqeust;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;

import io.swagger.v3.oas.annotations.media.Schema;
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

        @Size(max = 500, message = "리뷰는 300자 이하여야 합니다.")
        @NotEmpty(message = "리뷰 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @Setter
    public static class CreateRecreationDTO {

        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        @Size(max = 150, message = "제목은 150자를 넘을 수 없습니다.")
        String title;

        @Size(max = 3, message = "키워드는 3개를 넘을 수 없습니다.")
        List<Keyword> keywords;

        List<
                        @NotBlank(message = "해시태그 내용은 비워둘 수 없습니다.")
                        @Size(max = 30, message = "해시태그는 30자를 넘을 수 없습니다.") String>
                hashtags;

        @NotBlank(message = "소개는 비워둘 수 없습니다.")
        @Size(max = 300, message = "내용은 300자를 넘을 수 없습니다.")
        String summary;

        @NotBlank(message = "장소는 하나 이상 있어야합니다.")
        @Size(max = 2)
        List<Place> places;

        @Min(0)
        Integer playTime;

        @Size(max = 3, message = "목적은 3개를 넘을 수 없습니다.")
        List<Purpose> purposes;

        @Min(value = 1, message = "최소 참가자는 1명 이상이어야 합니다.")
        Integer minParticipants;

        @Min(value = 1, message = "최대 참가자는 1명 이상이어야 합니다.")
        Integer maxParticipants;

        List<String> preparations;

        List<Age> ages;

        List<Gender> genders;

        @Size(min = 1)
        List<CreateRecreationWayDTO> ways;
    }

    @Getter
    @Setter
    public static class CreateRecreationWayDTO {

        @NotBlank(message = "방법 내용은 비워둘 수 없습니다.")
        @Size(max = 300, message = "방법 내용은 300자를 넘을 수 없습니다.")
        String content;

        @Schema(description = "방법 순서, 0부터 시작")
        Integer seq;
    }
}
