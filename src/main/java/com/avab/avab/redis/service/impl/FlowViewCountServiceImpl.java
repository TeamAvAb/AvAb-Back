package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.stream.IntStream;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.avab.avab.redis.repository.FlowViewCountRepository;
import com.avab.avab.redis.service.FlowViewCountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlowViewCountServiceImpl implements FlowViewCountService {

    private final FlowViewCountRepository flowViewCountRepository;

    @Override
    public void incrementViewCount(Long id) {
        String flowId = id.toString();

        if (flowViewCountRepository.getViewCount(flowId).isEmpty()) {
            flowViewCountRepository.createViewCount(flowId);
        }

        flowViewCountRepository.incrementViewCount(flowId);
    }

    @Override
    public Long getViewCount(Long id) {
        String flowId = id.toString();

        String viewCount = flowViewCountRepository.getViewCount(flowId).orElse(null);

        return viewCount != null ? Long.valueOf(viewCount) : null;
    }

    @Override
    public List<Long> getAllFlowIds() {
        return flowViewCountRepository.getAllFlowIds().stream().map(Long::valueOf).toList();
    }

    @Override
    public void incrementViewCountLast7Days(Long id) {
        String flowId = id.toString();

        flowViewCountRepository.createViewCountLast7Days(flowId);
        flowViewCountRepository.incrementViewCountLast7Days(flowId);
    }

    @Override
    public Long getTotalViewCountLast7Days(Long id) {
        return IntStream.range(0, 7)
                .mapToObj(offset -> LocalDate.now().minusDays(offset))
                .map(
                        date -> {
                            String flowId = id.toString();
                            return flowViewCountRepository
                                    .getViewCountLast7Days(flowId, date)
                                    .orElse("0");
                        })
                .mapToLong(Long::valueOf)
                .sum();
    }

    @Override
    public List<Long> getAllFlowIdsToUpdateViewCountLast7Days() {
        return flowViewCountRepository.getAllFlowIdsToUpdateViewCountLast7Days().stream()
                .map(Long::valueOf)
                .toList();
    }
}
