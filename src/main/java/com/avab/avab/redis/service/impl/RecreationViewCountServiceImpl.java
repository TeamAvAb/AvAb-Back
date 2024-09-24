package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.joda.time.LocalDate;
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
    public Map<Long, Long> getTargetRecreationsViewCounts() {
        List<Long> targetRecreationIds = recreationViewCountRepository.getAllRecreationKeys();
        List<Long> viewCounts =
                recreationViewCountRepository.getViewCountsByIds(targetRecreationIds);

        return IntStream.range(0, targetRecreationIds.size())
                .boxed()
                .collect(Collectors.toMap(targetRecreationIds::get, viewCounts::get));
    }

    @Override
    public void incrementViewCountLast7Days(Long id) {
        String flowId = id.toString();

        recreationViewCountRepository.createViewCountLast7Days(flowId);
        recreationViewCountRepository.incrementViewCountLast7Days(flowId);
    }

    @Override
    public Long getTotalViewCountLast7Days(Long id) {
        return IntStream.range(0, 7)
                .mapToObj(offset -> LocalDate.now().minusDays(offset))
                .map(
                        date -> {
                            String recreationId = id.toString();
                            return recreationViewCountRepository
                                    .getViewCountLast7Days(recreationId, date)
                                    .orElse("0");
                        })
                .mapToLong(Long::valueOf)
                .sum();
    }

    @Override
    public List<Long> getAllFlowIdsToUpdateViewCountLast7Days() {
        return recreationViewCountRepository.getAllFlowIdsToUpdateViewCountLast7Days().stream()
                .map(Long::valueOf)
                .toList();
    }
}
