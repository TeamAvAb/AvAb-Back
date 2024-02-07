package com.avab.avab.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;

public interface FlowService {

    Page<Flow> getFlows(Integer page);

    Flow postFlow(PostFlowDTO postFlowDTO, User user);

    Flow getFlowDetail(Long flowId);

    Boolean existsByFlowId(Long flowId);

    List<Long> getUpdateTargetFlowIds(List<Long> flowIdList);

    void updateFlowViewCount(Long flowId, Long viewCount);
}
