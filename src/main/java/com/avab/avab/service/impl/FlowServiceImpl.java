package com.avab.avab.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.domain.Flow;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final FlowRepository flowRepository;

    public Flow getFlowDetail(Long flowId) {
        return flowRepository.findById(flowId).orElseThrow();
    }
}
