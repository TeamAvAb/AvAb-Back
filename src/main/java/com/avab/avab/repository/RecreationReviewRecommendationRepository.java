package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;

public interface RecreationReviewRecommendationRepository
        extends JpaRepository<RecreationReviewRecommendation, Long> {

    Optional<RecreationReviewRecommendation> findByRecreationReviewAndUser(
            RecreationReview review, User user);
}
