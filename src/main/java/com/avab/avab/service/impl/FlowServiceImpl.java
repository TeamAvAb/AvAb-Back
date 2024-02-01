package com.avab.avab.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowAge;
import com.avab.avab.domain.FlowGender;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.RecreationKeyWordRepository;
import com.avab.avab.repository.RecreationPurposeRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FlowServiceImpl implements FlowService {

    private final FlowRepository flowRepository;
    private final RecreationRepository recreationRepository;
    private final RecreationPurposeRepository recreationPurposeRepository;
    private final RecreationKeyWordRepository recreationKeywordRepository;

    private final Integer FLOW_LIST_PAGE_SIZE = 6;

    @Override
    public Page<Flow> getFlows(Integer page) {
        return flowRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, FLOW_LIST_PAGE_SIZE));
    }

    public Flow getFlowDetail(Long flowId) {
        return flowRepository.findById(flowId).orElseThrow();
    }

    public Flow postFlow(PostFlowDTO postFlowDTO, User user) {

        List<FlowAge> flowAgeList = new ArrayList<>();
        List<FlowFavorite> flowFavoriteList = new ArrayList<>();
        List<FlowRecreation> flowRecreationList = new ArrayList<>();
        List<FlowRecreationKeyword> flowRecreationKeywordList = new ArrayList<>();
        List<FlowGender> flowGenderList = new ArrayList<>();
        List<FlowRecreationPurpose> flowRecreationPurposeList = new ArrayList<>();

        Flow flow =
                FlowConverter.toFlow(
                        postFlowDTO,
                        user,
                        flowAgeList,
                        flowFavoriteList,
                        flowRecreationList,
                        flowRecreationKeywordList,
                        flowGenderList,
                        flowRecreationPurposeList);

        FlowConverter.addFlowRecreation(
                postFlowDTO, flow, recreationRepository, flowRecreationList);
        FlowConverter.addFlowAge(postFlowDTO, flow, flowAgeList);
        FlowConverter.addFlowGender(postFlowDTO, flow, flowGenderList);
        FlowConverter.addFlowRecreationKeyword(
                postFlowDTO, flow, recreationKeywordRepository, flowRecreationKeywordList);
        FlowConverter.addFlowRecreationPurpose(
                postFlowDTO, flow, recreationPurposeRepository, flowRecreationPurposeList);

        flowRepository.save(flow);
        return flow;
    }
}
