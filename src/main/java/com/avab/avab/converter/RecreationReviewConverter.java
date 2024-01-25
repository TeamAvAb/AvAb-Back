package com.avab.avab.converter;

import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationReviewRequestDTO.ToggleRecommendationDTO;
import com.avab.avab.dto.response.RecreationReviewResponseDTO.RecommendationDTO;

public class RecreationReviewConverter {

    public static RecreationReviewRecommendation toRecreationReviewRecommendation(
            User user, RecreationReview review, ToggleRecommendationDTO request) {
        return RecreationReviewRecommendation.builder()
                .recreationReview(review)
                .type(request.getType())
                .user(user)
                .build();
    }

    public static RecommendationDTO toRecommendationDTO(
            RecreationReviewRecommendation recommendation) {
        return RecommendationDTO.builder()
                .isRecommended(recommendation != null)
                .type(recommendation != null ? recommendation.getType() : null)
                .build();
    }
}
