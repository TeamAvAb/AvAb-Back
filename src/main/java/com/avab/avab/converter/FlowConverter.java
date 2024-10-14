package com.avab.avab.converter;

import com.avab.avab.domain.mapping.FlowScrap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.avab.avab.domain.CustomRecreation;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowAge;
import com.avab.avab.domain.FlowGender;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.dto.reqeust.FlowRequestDTO.RecreationSpec;
import com.avab.avab.dto.response.FlowResponseDTO.DeletedFlowDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowCreatedDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailPageDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowScrapDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationFlowDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;

@Component
public class FlowConverter {

    public static FlowPreviewPageDTO toFlowPreviewPageDTO(Page<Flow> flowPage, User user) {
        List<FlowPreviewDTO> flowPreviewDTOList =
                flowPage.stream().map(flow -> toFlowPreviewDTO(flow, user)).toList();

        return FlowPreviewPageDTO.builder()
                .flowList(flowPreviewDTOList)
                .totalPages(flowPage.getTotalPages())
                .build();
    }

    public static FlowPreviewDTO toFlowPreviewDTO(Flow flow, User user) {
        User author = flow.getAuthor();

        Boolean isScraped =
                user != null
                        ? flow.getFlowScrapList().stream()
                              .anyMatch((flowFavorite -> flowFavorite.getUser().equals(user)))
                        : null;

        List<Purpose> purposeList =
                flow.getFlowRecreationPurposeList().stream()
                        .map(FlowRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .toList();

        return FlowPreviewDTO.builder()
                .id(flow.getId())
                .title(flow.getTitle())
                .author(
                        AuthorDTO.builder()
                                .username(author.getUsername())
                                .userId(author.getId())
                                .build())
                .purpose(purposeList)
                .isScraped(isScraped)
                .totalPlayTime(flow.getTotalPlayTime())
                .scrapCount(flow.getScrapCount())
                .imageUrl(flow.getImageUrl())
                .viewCount(flow.getViewCount())
                .build();
    }

    public static Flow toFlow(
            PostFlowDTO request,
            User user,
            String imageUrl,
            Map<Integer, Recreation> recreationMap,
            Map<Integer, CustomRecreation> customRecreationMap,
            List<RecreationKeyword> recreationKeywordList,
            List<RecreationPurpose> recreationPurposeList) {

        Flow flow =
                Flow.builder()
                        .participants(request.getParticipants())
                        .totalPlayTime(request.getTotalPlayTime())
                        .title(request.getTitle())
                        .imageUrl(imageUrl)
                        .author(user)
                        .build();

        List<FlowRecreation> flowRecreationList =
                request.getRecreationSpecList().stream()
                        .map(
                                spec -> {
                                    // Recreation에 대한 FlowRecreation 생성
                                    if (recreationMap.containsKey(spec.getSeq())) {
                                        return FlowRecreation.builder()
                                                .flow(flow)
                                                .recreation(recreationMap.get(spec.getSeq()))
                                                .customPlayTime(spec.getCustomPlayTime())
                                                .seq(spec.getSeq())
                                                .build();
                                    }
                                    // CustomRecreation에 대한 FlowRecreation 생성
                                    return FlowRecreation.builder()
                                            .flow(flow)
                                            .customRecreation(
                                                    customRecreationMap.get(spec.getSeq()))
                                            .customPlayTime(spec.getCustomPlayTime())
                                            .seq(spec.getSeq())
                                            .build();
                                })
                        .toList();

        List<FlowRecreationKeyword> flowRecreationKeywordList =
                recreationKeywordList.stream()
                        .map(
                                recreationKeyword ->
                                        FlowRecreationKeyword.builder()
                                                .flow(flow)
                                                .keyword(recreationKeyword)
                                                .build())
                        .toList();

        List<FlowRecreationPurpose> flowRecreationPurposeList =
                recreationPurposeList.stream()
                        .map(
                                recreationPurpose ->
                                        FlowRecreationPurpose.builder()
                                                .flow(flow)
                                                .purpose(recreationPurpose)
                                                .build())
                        .toList();

        List<FlowAge> flowAgeList =
                request.getAgeList().stream()
                        .map(age -> FlowAge.builder().age(age).flow(flow).build())
                        .toList();

        List<FlowGender> flowGenderList =
                request.getGenderList().stream()
                        .map(gender -> FlowGender.builder().flow(flow).gender(gender).build())
                        .toList();

        flow.getFlowRecreationList().addAll(flowRecreationList);
        flow.getFlowRecreationKeywordList().addAll(flowRecreationKeywordList);
        flow.getFlowRecreationPurposeList().addAll(flowRecreationPurposeList);
        flow.getAgeList().addAll(flowAgeList);
        flow.getGenderList().addAll(flowGenderList);

        return flow;
    }

    public static CustomRecreation toCustomRecreation(
            RecreationSpec spec, List<RecreationKeyword> recreationKeywordList) {
        CustomRecreation customRecreation =
                CustomRecreation.builder()
                        .title(spec.getCustomTitle())
                        .playTime(spec.getCustomPlayTime())
                        .build();

        List<RecreationRecreationKeyword> recreationRecreationKeywordList =
                recreationKeywordList.stream()
                        .map(
                                keyword ->
                                        RecreationRecreationKeyword.builder()
                                                .keyword(keyword)
                                                .customRecreation(customRecreation)
                                                .build())
                        .toList();
        customRecreation
                .getRecreationRecreationKeywordList()
                .addAll(recreationRecreationKeywordList);
        return customRecreation;
    }

    public static FlowDetailPageDTO toFlowDetailDTO(Flow flow, User user) {

        List<Age> ageList =
                flow.getAgeList().stream().map(FlowAge::getAge).collect(Collectors.toList());

        List<Keyword> keywordList =
                flow.getFlowRecreationKeywordList().stream()
                        .map(FlowRecreationKeyword::getKeyword)
                        .map(RecreationKeyword::getKeyword)
                        .collect(Collectors.toList());

        List<Gender> genderList =
                flow.getGenderList().stream()
                        .map(FlowGender::getGender)
                        .collect(Collectors.toList());

        List<RecreationFlowDTO> recreationFlowListDTO =
                flow.getFlowRecreationList().stream()
                        .map(
                                flowRecreation -> {
                                    // 기존 Recreation 처리
                                    if (flowRecreation.getRecreation() != null) {
                                        return RecreationConverter.toRecreationFlowDTO(
                                                flowRecreation, user);
                                    }
                                    return RecreationConverter.toCustomRecreationFlowDTO(
                                            flowRecreation, user);
                                })
                        .toList();

        List<Purpose> purposeList =
                flow.getFlowRecreationPurposeList().stream()
                        .map(FlowRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .collect(Collectors.toList());

        Boolean isScraped =
                user != null
                        ? flow.getFlowScrapList().stream()
                              .anyMatch((flowFavorite -> flowFavorite.getUser().equals(user)))
                        : null;

        User author = flow.getAuthor();

        Integer scrapCount = flow.getFlowScrapList().size();

        FlowDetailPageDTO flowDetailPageDTO =
                FlowDetailPageDTO.builder()
                        .flowDetail(
                                FlowDetailDTO.builder()
                                        .id(flow.getId())
                                        .totalPlayTime(flow.getTotalPlayTime())
                                        .participants(flow.getParticipants())
                                        .viewCount(flow.getViewCount())
                                        .title(flow.getTitle())
                                        .age(ageList)
                                        .keywordList(keywordList)
                                        .gender(genderList)
                                        .purposeList(purposeList)
                                        .author(
                                                AuthorDTO.builder()
                                                        .userId(author.getId())
                                                        .username(author.getUsername())
                                                        .build())
                                        .isFavorite(isScraped)
                                        .scrapCount(scrapCount)
                                        .imageUrl(flow.getImageUrl())
                                        .build())
                        .recreations(recreationFlowListDTO)
                        .build();

        return flowDetailPageDTO;
    }

    public static List<FlowDetailPageDTO> toFlowDetailListDTO(List<Flow> flows, User user) {
        return flows.stream().map(flow -> toFlowDetailDTO(flow, user)).toList();
    }

    public static FlowScrapDTO toFlowScrapDTO(Boolean isScraped) {
        return FlowScrapDTO.builder().isScraped(isScraped).build();
    }

    public static FlowScrap toFlowFavorite(Flow flow, User user) {
        return FlowScrap.builder().flow(flow).user(user).build();
    }

    public static DeletedFlowDTO toDeletedFlowDTO(Long flowId) {
        return DeletedFlowDTO.builder().flowId(flowId).build();
    }

    public static FlowCreatedDTO toFlowCreatedDTO(Flow flow) {
        return FlowCreatedDTO.builder().flowId(flow.getId()).build();
    }

    public static Flow toUpdateFlow(
            Long flowId,
            PostFlowDTO request,
            User user,
            String imageUrl,
            Map<Integer, Recreation> recreationMap,
            Map<Integer, CustomRecreation> customRecreationMap,
            List<RecreationKeyword> recreationKeywordList,
            List<RecreationPurpose> recreationPurposeList) {
        Flow flow =
                Flow.builder()
                        .id(flowId)
                        .participants(request.getParticipants())
                        .totalPlayTime(request.getTotalPlayTime())
                        .imageUrl(imageUrl)
                        .title(request.getTitle())
                        .author(user)
                        .build();

        List<FlowRecreation> flowRecreationList =
                request.getRecreationSpecList().stream()
                        .map(
                                spec -> {
                                    // Recreation에 대한 FlowRecreation 생성
                                    if (recreationMap.containsKey(spec.getSeq())) {
                                        return FlowRecreation.builder()
                                                .flow(flow)
                                                .recreation(recreationMap.get(spec.getSeq()))
                                                .customPlayTime(spec.getCustomPlayTime())
                                                .seq(spec.getSeq())
                                                .build();
                                    }
                                    // CustomRecreation에 대한 FlowRecreation 생성
                                    return FlowRecreation.builder()
                                            .flow(flow)
                                            .customRecreation(
                                                    customRecreationMap.get(spec.getSeq()))
                                            .customPlayTime(spec.getCustomPlayTime())
                                            .seq(spec.getSeq())
                                            .build();
                                })
                        .toList();

        List<FlowRecreationKeyword> flowRecreationKeywordList =
                recreationKeywordList.stream()
                        .map(
                                recreationKeyword ->
                                        FlowRecreationKeyword.builder()
                                                .flow(flow)
                                                .keyword(recreationKeyword)
                                                .build())
                        .toList();

        List<FlowRecreationPurpose> flowRecreationPurposeList =
                recreationPurposeList.stream()
                        .map(
                                recreationPurpose ->
                                        FlowRecreationPurpose.builder()
                                                .flow(flow)
                                                .purpose(recreationPurpose)
                                                .build())
                        .toList();

        List<FlowAge> flowAgeList =
                request.getAgeList().stream()
                        .map(age -> FlowAge.builder().age(age).flow(flow).build())
                        .toList();

        List<FlowGender> flowGenderList =
                request.getGenderList().stream()
                        .map(gender -> FlowGender.builder().flow(flow).gender(gender).build())
                        .toList();

        flow.getFlowRecreationList().addAll(flowRecreationList);
        flow.getFlowRecreationKeywordList().addAll(flowRecreationKeywordList);
        flow.getFlowRecreationPurposeList().addAll(flowRecreationPurposeList);
        flow.getAgeList().addAll(flowAgeList);
        flow.getGenderList().addAll(flowGenderList);

        return flow;
    }
}
