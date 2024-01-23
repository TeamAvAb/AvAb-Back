package com.avab.avab.redis.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.redis.repository.RecreationViewCountRepository;
import com.avab.avab.redis.service.RecreationViewCountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecreationViewCountServiceImpl implements RecreationViewCountService {

    private final RecreationViewCountRepository recreationViewCountRepository;

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        String recreationId = id.toString();

        if (recreationViewCountRepository.getViewCount(recreationId) == null) {
            recreationViewCountRepository.createViewCount(recreationId);
        }

        recreationViewCountRepository.incrementViewCount(recreationId);
    }

    @Override
    public Long getViewCount(Long id) {
        String recreationId = id.toString();

        String viewCount = recreationViewCountRepository.getViewCount(recreationId);

        return viewCount != null ? Long.valueOf(viewCount) : null;
    }

    @Override
    public List<Long> getAllRecreationIds() {
        return recreationViewCountRepository.getAllRecreationIds().stream()
                .map(Long::valueOf)
                .toList();
    }
}
