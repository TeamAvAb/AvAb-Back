package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public void incrementViewCount(Long recreationId) {
        recreationViewCountRepository.createViewCountById(recreationId);
        recreationViewCountRepository.incrementViewCountById(recreationId);
    }

    @Override
    public Map<Long, Long> getTargetIdsAndViewCounts() {
        List<Long> targetRecreationIds = recreationViewCountRepository.getAllTargetIds();
        List<Long> viewCounts =
                recreationViewCountRepository.getViewCountsByIds(targetRecreationIds);

        return IntStream.range(0, targetRecreationIds.size())
                .boxed()
                .collect(Collectors.toMap(targetRecreationIds::get, viewCounts::get));
    }
}
