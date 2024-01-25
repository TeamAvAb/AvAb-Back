package com.avab.avab.dto.reqeust;

import jakarta.validation.constraints.NotNull;

import com.avab.avab.domain.enums.RecommendationType;

import lombok.Getter;
import lombok.Setter;

public class RecreationReviewRequestDTO {

    @Getter
    @Setter
    public static class ToggleRecommendationDTO {

        @NotNull(message = "추천 타입은 필수입니다.")
        private RecommendationType type;
    }
}
