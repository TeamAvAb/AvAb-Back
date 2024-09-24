package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    public void incrementViewCount(Long flowId) {
        flowViewCountRepository.createViewCountById(flowId);
        flowViewCountRepository.incrementViewCountById(flowId);
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

    @Override
    public Map<Long, Long> getTargetFlowsViewCounts() {
        List<Long> targetFlowIds = flowViewCountRepository.getAllFlowIds();
        List<Long> viewCounts = flowViewCountRepository.getViewCountsByIds(targetFlowIds);

        return IntStream.range(0, targetFlowIds.size())
                .boxed()
                .collect(Collectors.toMap(targetFlowIds::get, viewCounts::get));
    }
}
