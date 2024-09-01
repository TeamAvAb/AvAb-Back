package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.enums.UserStatus;

public interface RecreationReviewRepository extends JpaRepository<RecreationReview, Long> {

    Page<RecreationReview> findAllByRecreationAndIdNotInAndDeletedAtIsNullAndAuthor_UserStatusNot(
            Recreation recreation, List<Long> notInIds, UserStatus userStatus, Pageable pageable);

    Page<RecreationReview> findAllByRecreationAndDeletedAtIsNullAndAuthor_UserStatusNot(
            Recreation recreation, UserStatus userStatus, Pageable pageable);
}
