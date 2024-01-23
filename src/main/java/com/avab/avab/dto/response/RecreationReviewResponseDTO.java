package com.avab.avab.dto.response;

import com.avab.avab.domain.enums.RecommendationType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecreationReviewResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecommendationDTO {

        Boolean isRecommended;

        @Schema(description = "추천이면 GOOD, 비추이면 BAD")
        RecommendationType type;
    }
}
