package com.avab.avab.converter;

import java.util.List;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.dto.response.FlowResponseDTO;
import com.avab.avab.dto.response.RecreationResponseDTO;
import com.avab.avab.dto.response.UserResponseDTO;

public class UserConverter {

    public static UserResponseDTO.UserResponse toUserResponse(User user) {
        return UserResponseDTO.UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .socialType(user.getSocialType())
                .build();
    }

    public static FlowResponseDTO.FlowPreviewPageDTO toFlowPreviewPageDTO(
            Page<Flow> flowPage, User user) {
        List<FlowResponseDTO.FlowPreviewDTO> flowPreviewDTOList =
                flowPage.stream().map(flow -> toFlowPreviewDTO(flow, user)).toList();

        return FlowResponseDTO.FlowPreviewPageDTO.builder()
                .flowList(flowPreviewDTOList)
                .totalPages(flowPage.getTotalPages())
                .build();
    }

    private static FlowResponseDTO.FlowPreviewDTO toFlowPreviewDTO(Flow flow, User user) {
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

        return FlowResponseDTO.FlowPreviewDTO.builder()
                .id(flow.getId())
                .title(flow.getTitle())
                .author(
                        RecreationResponseDTO.RecreationReviewDTO.AuthorDTO.builder()
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
