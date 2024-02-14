package com.avab.avab.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.FlowException;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.CustomRecreation;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.dto.reqeust.FlowRequestDTO.RecreationSpec;
import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.repository.CustomRecreationRepository;
import com.avab.avab.repository.FlowFavoriteRepository;
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
    private final FlowFavoriteRepository flowFavoriteRepository;
    private final FlowViewCountService flowViewCountService;
    private final RecreationRepository recreationRepository;
    private final RecreationPurposeRepository recreationPurposeRepository;
    private final RecreationKeywordRepository recreationKeywordRepository;
    private final CustomRecreationRepository customRecreationRepository;

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

    @Override
    @Transactional
    public Boolean toggleScrapeFlow(User user, Long flowId) {
        Flow flow =
                flowRepository
                        .findById(flowId)
                        .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));
        Optional<FlowFavorite> flowFavorite = flowFavoriteRepository.findByFlowAndUser(flow, user);

        if (flowFavorite.isPresent()) {
            flowFavoriteRepository.delete(flowFavorite.get());
            flowRepository.decrementScrapCountById(flow.getId());

            return false;
        }

        FlowFavorite favorite = FlowConverter.toFlowFavorite(flow, user);
        flowFavoriteRepository.save(favorite);
        flowRepository.incrementScrapCountById(flow.getId());

        return true;
    }

    @Transactional
    public Flow postFlow(PostFlowDTO postFlowDTO, User user) {
        Map<Long, Recreation> recreationMap = new HashMap<>();
        Map<String, CustomRecreation> customRecreationMap = new HashMap<>();

        for (RecreationSpec spec : postFlowDTO.getRecreationSpecList()) {
            if (spec.getRecreationId() != null) {
                Recreation recreation =
                        recreationRepository
                                .findById(spec.getRecreationId())
                                .orElseThrow(
                                        () ->
                                                new RecreationException(
                                                        ErrorStatus.RECREATION_NOT_FOUND));
                if (recreation != null) {
                    recreationMap.put(spec.getRecreationId(), recreation);
                }
            } else if (spec.getCustomTitle() != null) {
                List<RecreationKeyword> customRecreationKeywordList =
                        spec.getCustomKeywordList().stream()
                                .map(
                                        keyword ->
                                                recreationKeywordRepository
                                                        .findByKeyword(keyword)
                                                        .get())
                                .toList();

                CustomRecreation customRecreation =
                        FlowConverter.toCustomRecreation(spec, customRecreationKeywordList);

                customRecreationRepository.save(customRecreation);
                customRecreationMap.put(spec.getCustomTitle(), customRecreation);
            }
        }

        List<RecreationKeyword> recreationKeywordList =
                postFlowDTO.getKeywordList().stream()
                        .map(
                                keyword ->
                                        recreationKeywordRepository
                                                .findByKeyword(keyword)
                                                .orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        List<RecreationPurpose> recreationPurposeList =
                postFlowDTO.getPurposeList().stream()
                        .map(
                                purpose ->
                                        recreationPurposeRepository
                                                .findByPurpose(purpose)
                                                .orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        Flow flow =
                FlowConverter.toFlow(
                        postFlowDTO,
                        user,
                        recreationMap,
                        customRecreationMap,
                        recreationKeywordList,
                        recreationPurposeList);

        flowRepository.save(flow);

        return flow;
    }

    @Transactional
    public void deleteFlow(Long flowId, User user) {
        Flow flow =
                flowRepository
                        .findById(flowId)
                        .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));
        if (!flow.getAuthor().getId().equals(user.getId())) {
            throw new FlowException(ErrorStatus.FLOW_DELETE_UNAUTHORIZED);
        }
        flowRepository.delete(flow);
    }
}
