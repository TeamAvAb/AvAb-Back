package com.avab.avab.redis.service.impl;

import java.util.List;

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
}
