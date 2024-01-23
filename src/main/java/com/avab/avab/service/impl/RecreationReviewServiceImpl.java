package com.avab.avab.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationReviewException;
import com.avab.avab.converter.RecreationReviewConverter;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
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

        Optional<RecreationReviewRecommendation> recommendation =
                recreationReviewRecommendationRepository.findByRecreationReviewAndUser(
                        review, user);

        RecreationReviewRecommendation updatedRecommendation = null;
        if (recommendation.isEmpty()) {
            updatedRecommendation =
                    RecreationReviewConverter.toRecreationReviewRecommendation(
                            user, review, request);
            return recreationReviewRecommendationRepository.save(updatedRecommendation);
        }

        updatedRecommendation = recommendation.get();
        if (updatedRecommendation.getType().equals(request.getType())) {
            recreationReviewRecommendationRepository.delete(updatedRecommendation);
            return null;
        }

        updatedRecommendation.toggleType();
        return updatedRecommendation;
    }

    @Override
    public Boolean existsById(Long reviewId) {
        return recreationReviewRepository.existsById(reviewId);
    }
}
