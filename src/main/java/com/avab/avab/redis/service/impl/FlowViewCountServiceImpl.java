package com.avab.avab.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public Map<Long, Long> getTargetIdsAndViewCounts() {
        List<Long> targetFlowIds = flowViewCountRepository.getAllTargetIds();
        List<Long> viewCounts = flowViewCountRepository.getViewCountsByIds(targetFlowIds);

        return IntStream.range(0, targetFlowIds.size())
                .boxed()
                .collect(Collectors.toMap(targetFlowIds::get, viewCounts::get));
    }
}
