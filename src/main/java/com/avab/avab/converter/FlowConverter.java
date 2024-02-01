package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;
import com.avab.avab.repository.RecreationKeyWordRepository;
import com.avab.avab.repository.RecreationPurposeRepository;
import com.avab.avab.repository.RecreationRepository;

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
                        ? flow.getFlowFavoriteList().stream()
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
                .viewCount(flow.getViewCount())
                .build();
    }

    public static Flow toFlow(
            PostFlowDTO postFlowDTO,
            User user,
            List<FlowAge> flowAgeList,
            List<FlowFavorite> flowFavoriteList,
            List<FlowRecreation> flowRecreationList,
            List<FlowRecreationKeyword> flowRecreationKeywordList,
            List<FlowGender> flowGenderList,
            List<FlowRecreationPurpose> flowRecreationPurposeList) {

        Flow flow =
                Flow.builder()
                        .participants(postFlowDTO.getParticipants())
                        .totalPlayTime(postFlowDTO.getTotalPlayTime())
                        .ageList(flowAgeList)
                        .flowFavoriteList(flowFavoriteList)
                        .flowRecreationList(flowRecreationList)
                        .flowRecreationKeywordList(flowRecreationKeywordList)
                        .genderList(flowGenderList)
                        .flowRecreationPurposeList(flowRecreationPurposeList)
                        .title(postFlowDTO.getTitle())
                        .author(user)
                        .build();

        return flow;
    }

    public static void addFlowRecreation(
            PostFlowDTO postFlowDTO,
            Flow flow,
            RecreationRepository recreationRepository,
            List<FlowRecreation> flowRecreationList) {
        postFlowDTO.getRecreationSpecList().stream()
                .flatMap(
                        recreationSpec ->
                                recreationRepository
                                        .findById(recreationSpec.getRecreationId())
                                        .map(
                                                recreation ->
                                                        FlowRecreation.builder()
                                                                .flow(flow)
                                                                .recreation(recreation)
                                                                .customPlayTime(
                                                                        recreationSpec
                                                                                .getCustomPlayTime())
                                                                .seq(recreationSpec.getSeq())
                                                                .build())
                                        .stream())
                .forEach(flowRecreationList::add);
    }

    public static void addFlowAge(PostFlowDTO postFlowDTO, Flow flow, List<FlowAge> flowAgeList) {
        postFlowDTO
                .getAgeList()
                .forEach(age -> flowAgeList.add(FlowAge.builder().age(age).flow(flow).build()));
    }

    public static void addFlowGender(
            PostFlowDTO postFlowDTO, Flow flow, List<FlowGender> flowGenderList) {
        postFlowDTO
                .getGenderList()
                .forEach(
                        gender ->
                                flowGenderList.add(
                                        FlowGender.builder().flow(flow).gender(gender).build()));
    }

    public static void addFlowRecreationKeyword(
            PostFlowDTO postFlowDTO,
            Flow flow,
            RecreationKeyWordRepository recreationKeywordRepository,
            List<FlowRecreationKeyword> flowRecreationKeywordList) {
        postFlowDTO
                .getKeywordList()
                .forEach(
                        keyword -> {
                            RecreationKeyword recreationKeyword =
                                    recreationKeywordRepository.findByKeyword(keyword);
                            FlowRecreationKeyword flowRecreationKeyword =
                                    FlowRecreationKeyword.builder()
                                            .flow(flow)
                                            .keyword(recreationKeyword)
                                            .build();
                            flowRecreationKeywordList.add(flowRecreationKeyword);
                        });
    }

    public static void addFlowRecreationPurpose(
            PostFlowDTO postFlowDTO,
            Flow flow,
            RecreationPurposeRepository recreationPurposeRepository,
            List<FlowRecreationPurpose> flowRecreationPurposeList) {
        postFlowDTO
                .getPurposeList()
                .forEach(
                        purpose -> {
                            RecreationPurpose recreationPurpose =
                                    recreationPurposeRepository.findByPurpose(purpose);
                            FlowRecreationPurpose flowRecreationPurpose =
                                    FlowRecreationPurpose.builder()
                                            .flow(flow)
                                            .purpose(recreationPurpose)
                                            .build();
                            flowRecreationPurposeList.add(flowRecreationPurpose);
                        });
    }

    public static FlowDetailDTO toFlowDetailDTO(Flow flow, User user) {
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

        List<Recreation> recreationList =
                flow.getFlowRecreationList().stream()
                        .map(FlowRecreation::getRecreation)
                        .collect(Collectors.toList());

        List<RecreationPreviewDTO> recreationPreviewListDTO =
                RecreationConverter.toRecreationPreviewListDTO(recreationList, user);

        List<Purpose> purposeList =
                flow.getFlowRecreationPurposeList().stream()
                        .map(FlowRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .collect(Collectors.toList());

        Boolean isScraped =
                user != null
                        ? flow.getFlowFavoriteList().stream()
                                .anyMatch((flowFavorite -> flowFavorite.getUser().equals(user)))
                        : null;

        User author = flow.getAuthor();

        FlowDetailDTO flowDetailDTO =
                FlowDetailDTO.builder()
                        .id(flow.getId())
                        .totalPlayTime(flow.getTotalPlayTime())
                        .participants(flow.getParticipants())
                        .viewCount(flow.getViewCount())
                        .title(flow.getTitle())
                        .age(ageList)
                        .keywordList(keywordList)
                        .gender(genderList)
                        .recreations(recreationPreviewListDTO)
                        .purposeList(purposeList)
                        .author(
                                AuthorDTO.builder()
                                        .userId(author.getId())
                                        .username(author.getUsername())
                                        .build())
                        .isFavorite(isScraped)
                        .build();

        return flowDetailDTO;
    }
}
