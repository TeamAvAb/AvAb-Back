package com.avab.avab.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.RecreationKeywordRepository;
import com.avab.avab.repository.RecreationPurposeRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlowServiceImpl implements FlowService {

    private final FlowRepository flowRepository;
    private final RecreationRepository recreationRepository;
    private final RecreationPurposeRepository recreationPurposeRepository;
    private final RecreationKeywordRepository recreationKeywordRepository;

    private final Integer FLOW_LIST_PAGE_SIZE = 6;

    @Override
    public Page<Flow> getFlows(Integer page) {
        return flowRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, FLOW_LIST_PAGE_SIZE));
    }

    public Flow getFlowDetail(Long flowId) {
        return flowRepository.findById(flowId).orElseThrow();
    }

    @Transactional
    public Flow postFlow(PostFlowDTO postFlowDTO, User user) {

        List<Recreation> recreationList =
                postFlowDTO.getRecreationSpecList().stream()
                        .map(
                                recreationSpec ->
                                        recreationRepository
                                                .findById(recreationSpec.getRecreationId())
                                                .get())
                        .toList();

        List<RecreationKeyword> recreationKeywordList =
                postFlowDTO.getKeywordList().stream()
                        .map(recreationKeywordRepository::findByKeyword)
                        .toList();

        List<RecreationPurpose> recreationPurposeList =
                postFlowDTO.getPurposeList().stream()
                        .map(recreationPurposeRepository::findByPurpose)
                        .toList();

        Flow flow =
                FlowConverter.toFlow(
                        postFlowDTO,
                        user,
                        recreationList,
                        recreationKeywordList,
                        recreationPurposeList);

        flowRepository.save(flow);
        return flow;
    }
}
