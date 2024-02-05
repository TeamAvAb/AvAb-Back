package com.avab.avab.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.FlowException;
import com.avab.avab.domain.Flow;
import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlowServiceImpl implements FlowService {

    private final FlowRepository flowRepository;
    private final FlowViewCountService flowViewCountService;

    private final Integer FLOW_LIST_PAGE_SIZE = 6;

    @Override
    public Page<Flow> getFlows(Integer page) {
        return flowRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, FLOW_LIST_PAGE_SIZE));
    }

    public Flow getFlowDetail(Long flowId) {
        Flow flow =
                flowRepository
                        .findById(flowId)
                        .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));

        flowViewCountService.incrementViewCount(flowId);

        return flow;
    }

    @Override
    public Boolean existsByFlowId(Long flowId) {
        return flowRepository.existsById(flowId);
    }

    @Override
    public List<Long> getUpdateTargetFlowIds(List<Long> flowIdList) {
        return flowRepository.findAllByIdIn(flowIdList).stream().map(Flow::getId).toList();
    }

    @Override
    @Transactional
    public void updateFlowViewCount(Long flowId, Long viewCount) {
        flowRepository.incrementViewCountById(flowId, viewCount);
    }
}
