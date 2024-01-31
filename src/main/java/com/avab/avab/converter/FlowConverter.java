package com.avab.avab.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
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

    private static FlowPreviewDTO toFlowPreviewDTO(Flow flow, User user) {
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
}
