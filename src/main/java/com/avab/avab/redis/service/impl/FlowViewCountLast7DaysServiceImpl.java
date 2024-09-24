package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.avab.avab.redis.repository.FlowViewCountLast7DaysRepository;
import com.avab.avab.redis.service.FlowViewCountLast7DaysService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlowViewCountLast7DaysServiceImpl implements FlowViewCountLast7DaysService {
    private final FlowViewCountLast7DaysRepository flowViewCountLast7DaysRepository;

    @Override
    public void incrementViewCount(Long flowId) {
        flowViewCountLast7DaysRepository.createViewCountById(flowId);
        flowViewCountLast7DaysRepository.incrementViewCountById(flowId);
    }

    @Override
    public Map<Long, Long> getTargetIdsAndViewCounts() {
        List<Long> targetFlowIds = flowViewCountLast7DaysRepository.getAllTargetIds();
        List<Long> viewCounts = flowViewCountLast7DaysRepository.getViewCountsByIds(targetFlowIds);

        return IntStream.range(0, targetFlowIds.size())
                .boxed()
                .collect(Collectors.toMap(targetFlowIds::get, viewCounts::get));
    }
}
