package com.avab.avab.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.FlowException;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.controller.enums.SortCondition;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.CustomRecreation;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.enums.ReportType;
import com.avab.avab.domain.enums.UserStatus;
import com.avab.avab.domain.mapping.FlowScrap;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.redis.service.FlowViewCountLast7DaysService;
import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.repository.CustomRecreationRepository;
import com.avab.avab.repository.FlowAgeRepository;
import com.avab.avab.repository.FlowGenderRepository;
import com.avab.avab.repository.FlowRecreationKeywordRepository;
import com.avab.avab.repository.FlowRecreationPurposeRepository;
import com.avab.avab.repository.FlowRecreationRepository;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.FlowScrapRepository;
import com.avab.avab.repository.RecreationKeywordRepository;
import com.avab.avab.repository.RecreationPurposeRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlowServiceImpl implements FlowService {

    private final FlowViewCountService flowViewCountService;
    private final FlowViewCountLast7DaysService flowViewCountLast7DaysService;

    private final FlowRepository flowRepository;
    private final FlowScrapRepository flowScrapRepository;
    private final RecreationRepository recreationRepository;
    private final RecreationPurposeRepository recreationPurposeRepository;
    private final RecreationKeywordRepository recreationKeywordRepository;
    private final CustomRecreationRepository customRecreationRepository;
    private final FlowAgeRepository flowAgeRepository;
    private final FlowRecreationPurposeRepository flowRecreationPurposeRepository;
    private final FlowRecreationKeywordRepository flowRecreationKeywordRepository;
    private final FlowGenderRepository flowGenderRepository;
    private final FlowRecreationRepository flowRecreationRepository;

    private final String[] flowImageUrl = {
        "https://avab-bucket.s3.ap-northeast-3.amazonaws.com/flow/thumbnail/flow1.png",
        "https://avab-bucket.s3.ap-northeast-3.amazonaws.com/flow/thumbnail/flow2.png",
        "https://avab-bucket.s3.ap-northeast-3.amazonaws.com/flow/thumbnail/flow3.png",
        "https://avab-bucket.s3.ap-northeast-3.amazonaws.com/flow/thumbnail/flow4.png"
    };
    private final Random flowNumber = new Random();

    private final Integer FLOW_LIST_PAGE_SIZE = 6;

    @Override
    public Page<Flow> getFlows(User user, Integer page, SortCondition sortCondition) {
        TypedSort<Flow> flowSort = Sort.sort(Flow.class);
        Sort sort;
        switch (sortCondition) {
            case SCRAP -> sort = flowSort.by(Flow::getScrapCount).descending();
            case VIEW -> sort = flowSort.by(Flow::getViewCountLast7Days).descending();
            case RECENT -> sort = flowSort.by(Flow::getCreatedAt).descending();
            default -> throw new FlowException(ErrorStatus.INVALID_SORT_CONDITION);
        }

        List<Long> reportedFlowIds = new ArrayList<>();
        if (user != null) {
            reportedFlowIds =
                    user.getReportList().stream()
                            .filter(report -> report.getReportType() == ReportType.FLOW)
                            .map(Report::getTargetFlow)
                            .map(Flow::getId)
                            .toList();
        }

        return reportedFlowIds.isEmpty()
                ? flowRepository.findAllByDeletedAtIsNullAndAuthor_UserStatusNot(
                        UserStatus.DELETED, PageRequest.of(page, FLOW_LIST_PAGE_SIZE, sort))
                : flowRepository.findAllByIdNotInAndDeletedAtIsNullAndAuthor_UserStatusNot(
                        reportedFlowIds,
                        UserStatus.DELETED,
                        PageRequest.of(page, FLOW_LIST_PAGE_SIZE, sort));
    }

    public Flow getFlowDetail(User user, Long flowId) {
        Flow flow =
                flowRepository
                        .findByIdAndDeletedAtIsNullAndAuthor_UserStatusNot(
                                flowId, UserStatus.DELETED)
                        .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));

        if (user != null
                && flow.getReportList().stream()
                        .map(Report::getReporter)
                        .map(User::getId)
                        .anyMatch(id -> id.equals(user.getId()))) {
            throw new FlowException(ErrorStatus.REPORTED_FLOW);
        }

        flowViewCountService.incrementViewCount(flowId);
        flowViewCountLast7DaysService.incrementViewCount(flowId);

        return flow;
    }

    @Override
    public Boolean existsByFlowId(Long flowId) {
        return flowRepository.existsById(flowId);
    }

    @Override
    @Transactional
    public void incrementViewCountById(Long flowId, Long viewCount) {
        flowRepository.incrementViewCountById(flowId, viewCount);
    }

    @Override
    @Transactional
    public Boolean toggleScrapeFlow(User user, Long flowId) {
        Flow flow = getFlow(flowId);
        Optional<FlowScrap> flowFavorite = flowScrapRepository.findByFlowAndUser(flow, user);

        if (flowFavorite.isPresent()) {
            flowScrapRepository.delete(flowFavorite.get());
            flowRepository.decrementScrapCountById(flow.getId());

            return false;
        }

        FlowScrap favorite = FlowConverter.toFlowScrap(flow, user);
        flowScrapRepository.save(favorite);
        flowRepository.incrementScrapCountById(flow.getId());

        return true;
    }

    @Transactional
    public Flow postFlow(PostFlowDTO request, User user) {
        Map<Integer, Recreation> recreationMap = extractRecreationMap(request);
        Map<Integer, CustomRecreation> customRecreationMap = extractCustomRecreationMap(request);

        List<RecreationKeyword> recreationKeywordList =
                getRecreationKeywords(request.getKeywordList());
        List<RecreationPurpose> recreationPurposeList =
                getRecreationPurposes(request.getPurposeList());

        return createOrUpdateFlow(
                request,
                user,
                recreationMap,
                customRecreationMap,
                recreationKeywordList,
                recreationPurposeList,
                null);
    }

    @Transactional
    public void deleteFlow(Long flowId, User user) {
        Flow flow = getFlow(flowId);
        if (!flow.isAuthoredBy(user)) {
            throw new FlowException(ErrorStatus.FLOW_DELETE_UNAUTHORIZED);
        }

        flow.softDelete();
    }

    @Override
    public List<Flow> recommendFlows(
            List<Keyword> keywords,
            Integer participants,
            Integer totalPlayTime,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            User user) {
        return flowRepository.recommendFlows(
                keywords, participants, totalPlayTime, purposes, genders, ages, user);
    }

    @Override
    @Transactional
    public Flow updateFlow(PostFlowDTO request, User user, Long flowId) {
        Flow flow = getFlow(flowId);

        // custom, ageList, purposeList, genderList, keywordList는 시작하자마자 삭제 (일단 모두 삭제로 구현)
        flowRecreationKeywordRepository.deleteAllByFlow(flow);
        flowRecreationPurposeRepository.deleteAllByFlow(flow);
        flowGenderRepository.deleteAllByFlow(flow);
        flowAgeRepository.deleteAllByFlow(flow);
        flowRecreationRepository.deleteAllByFlow(flow);

        Map<Integer, Recreation> recreationMap = extractRecreationMap(request);
        Map<Integer, CustomRecreation> customRecreationMap = extractCustomRecreationMap(request);

        List<RecreationKeyword> recreationKeywordList =
                getRecreationKeywords(request.getKeywordList());
        List<RecreationPurpose> recreationPurposeList =
                getRecreationPurposes(request.getPurposeList());

        return createOrUpdateFlow(
                request,
                user,
                recreationMap,
                customRecreationMap,
                recreationKeywordList,
                recreationPurposeList,
                flowId);
    }

    @Override
    @Transactional
    public void incrementViewCountLast7Days(Long flowId, Long viewCount) {
        flowRepository.updateViewCountLast7DaysById(flowId, viewCount);
    }

    private Map<Integer, Recreation> extractRecreationMap(PostFlowDTO request) {
        Map<Integer, Recreation> recreationMap = new HashMap<>();

        for (var spec : request.getRecreationSpecList()) {
            if (spec.getRecreationId() != null) {
                Recreation recreation = getRecreation(spec.getRecreationId());
                recreationMap.put(spec.getSeq(), recreation);
            }
        }

        return recreationMap;
    }

    private Map<Integer, CustomRecreation> extractCustomRecreationMap(PostFlowDTO request) {
        Map<Integer, CustomRecreation> customRecreationMap = new HashMap<>();

        for (var spec : request.getRecreationSpecList()) {
            if (spec.getRecreationId() == null) {
                List<RecreationKeyword> keywords = new ArrayList<>();
                if (spec.getCustomKeywordList() != null) {
                    keywords = getRecreationKeywords(spec.getCustomKeywordList());
                }
                CustomRecreation customRecreation =
                        FlowConverter.toCustomRecreation(spec, keywords);
                customRecreationRepository.save(customRecreation);
                customRecreationMap.put(spec.getSeq(), customRecreation);
            }
        }
        return customRecreationMap;
    }

    private Flow createOrUpdateFlow(
            PostFlowDTO request,
            User user,
            Map<Integer, Recreation> recreationMap,
            Map<Integer, CustomRecreation> customRecreationMap,
            List<RecreationKeyword> recreationKeywordList,
            List<RecreationPurpose> recreationPurposeList,
            @Nullable Long flowId) {
        int num = flowNumber.nextInt(4);

        Flow flow =
                (flowId == null)
                        ? FlowConverter.toFlow(
                                request,
                                user,
                                flowImageUrl[num],
                                recreationMap,
                                customRecreationMap,
                                recreationKeywordList,
                                recreationPurposeList)
                        : FlowConverter.toUpdateFlow(
                                flowId,
                                request,
                                user,
                                flowImageUrl[num],
                                recreationMap,
                                customRecreationMap,
                                recreationKeywordList,
                                recreationPurposeList);

        return flowRepository.save(flow);
    }

    private Flow getFlow(Long flowId) {
        return flowRepository
                .findById(flowId)
                .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));
    }

    private Recreation getRecreation(Long recreationId) {
        return recreationRepository
                .findById(recreationId)
                .orElseThrow(() -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));
    }

    private List<RecreationKeyword> getRecreationKeywords(List<Keyword> keywords) {
        return keywords.stream()
                .map(keyword -> recreationKeywordRepository.findByKeyword(keyword).get())
                .toList();
    }

    private List<RecreationPurpose> getRecreationPurposes(List<Purpose> purposes) {
        return purposes.stream()
                .map(purpose -> recreationPurposeRepository.findByPurpose(purpose).get())
                .toList();
    }
}
