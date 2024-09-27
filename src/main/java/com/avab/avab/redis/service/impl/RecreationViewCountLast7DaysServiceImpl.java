package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.avab.avab.redis.repository.RecreationViewCountLast7DaysRepository;
import com.avab.avab.redis.service.RecreationViewCountLast7DaysService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecreationViewCountLast7DaysServiceImpl
        implements RecreationViewCountLast7DaysService {
    private final RecreationViewCountLast7DaysRepository recreationViewCountLast7DaysRepository;

    @Override
    public void incrementViewCount(Long recreationId) {
        recreationViewCountLast7DaysRepository.createViewCountById(recreationId);
        recreationViewCountLast7DaysRepository.incrementViewCountById(recreationId);
    }

    @Override
    public Map<Long, Long> getTargetIdsAndViewCounts() {
        List<Long> targetRecreationIds = recreationViewCountLast7DaysRepository.getAllTargetIds();
        List<Long> viewCounts =
                recreationViewCountLast7DaysRepository.getViewCountsByIds(targetRecreationIds);

        return IntStream.range(0, targetRecreationIds.size())
                .boxed()
                .collect(Collectors.toMap(targetRecreationIds::get, viewCounts::get));
    }
}
