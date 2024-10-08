package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.enums.UserStatus;

public interface RecreationReviewRepository extends JpaRepository<RecreationReview, Long> {
    Optional<RecreationReview> findByIdAndDeletedAtIsNullAndAuthor_UserStatusNot(
            Long id, UserStatus userStatus);
}
