package com.avab.avab.service;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;

public interface FlowService {

    Page<Flow> getFlows(Integer page);

    Flow getFlowDetail(Long flowId);

    Boolean existsByFlowId(Long flowId);
}
