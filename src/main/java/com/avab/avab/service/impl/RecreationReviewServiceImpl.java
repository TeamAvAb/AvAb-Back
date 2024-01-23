package com.avab.avab.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.repository.RecreationReviewRepository;
import com.avab.avab.service.RecreationReviewService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecreationReviewServiceImpl implements RecreationReviewService {

    private final RecreationReviewRepository recreationReviewRepository;

    @Override
    public Boolean existsById(Long reviewId) {
        return recreationReviewRepository.existsById(reviewId);
    }
}
