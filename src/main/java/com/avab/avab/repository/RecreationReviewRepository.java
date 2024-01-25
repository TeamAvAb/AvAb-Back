package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;

public interface RecreationReviewRepository extends JpaRepository<RecreationReview, Long> {

    Page<RecreationReview> findByRecreation(Recreation recreation, Pageable pageable);
}
