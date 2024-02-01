package com.avab.avab.service;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;

public interface FlowService {

    Page<Flow> getFlows(Integer page);

    Flow postFlow(PostFlowDTO postFlowDTO, User user);
}
