package com.avab.avab.service;

import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationReviewRequestDTO.ToggleRecommendationDTO;

public interface RecreationReviewService {

    RecreationReviewRecommendation toggleRecommendation(
            User user, Long reviewId, ToggleRecommendationDTO request);

    Boolean existsById(Long reviewId);
}
