package com.avab.avab.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationReviewException;
import com.avab.avab.converter.RecreationReviewConverter;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.RecommendationType;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationReviewRequestDTO.ToggleRecommendationDTO;
import com.avab.avab.repository.RecreationReviewRecommendationRepository;
import com.avab.avab.repository.RecreationReviewRepository;
import com.avab.avab.service.RecreationReviewService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecreationReviewServiceImpl implements RecreationReviewService {

    private final RecreationReviewRepository recreationReviewRepository;
    private final RecreationReviewRecommendationRepository recreationReviewRecommendationRepository;

    @Override
    @Transactional
    public RecreationReviewRecommendation toggleRecommendation(
            User user, Long reviewId, ToggleRecommendationDTO request) {
        RecreationReview review =
                recreationReviewRepository
                        .findById(reviewId)
                        .orElseThrow(
                                () -> new RecreationReviewException(ErrorStatus.REVIEW_NOT_FOUND));

        Optional<RecreationReviewRecommendation> queryRecommendation =
                recreationReviewRecommendationRepository.findByRecreationReviewAndUser(
                        review, user);

        RecreationReviewRecommendation recommendation;

        if (queryRecommendation.isEmpty()) {
            recommendation =
                    RecreationReviewConverter.toRecreationReviewRecommendation(
                            user, review, request);

            recreationReviewRecommendationRepository.save(recommendation);

            if (request.getType().equals(RecommendationType.GOOD)) {
                review.incrementGoodCount();
            } else {
                review.incrementBadCount();
            }

            return recommendation;
        }

        recommendation = queryRecommendation.get();
        if (recommendation.getType().equals(request.getType())) {
            recreationReviewRecommendationRepository.delete(recommendation);
            return null;
        }

        recreationReviewRecommendationRepository.delete(recommendation);
        recommendation =
                RecreationReviewConverter.toRecreationReviewRecommendation(user, review, request);
        recreationReviewRecommendationRepository.save(recommendation);

        if (request.getType().equals(RecommendationType.GOOD)) {
            review.incrementGoodCount();
            review.decrementBadCount();
        } else {
            review.incrementBadCount();
            review.decrementGoodCount();
        }

        return recommendation;
    }

    @Override
    public Boolean existsById(Long reviewId) {
        return recreationReviewRepository.existsById(reviewId);
    }
}
