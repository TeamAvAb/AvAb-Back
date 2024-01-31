package com.avab.avab.dto.response;

import java.util.List;

import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FlowResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowPreviewPageDTO {

        List<FlowPreviewDTO> flowList;
        Integer totalPages;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowPreviewDTO {

        Long id;
        List<Purpose> purpose;
        String title;
        Integer totalPlayTime;
        Long viewCount;
        AuthorDTO author;
        Long scrapCount;
        Boolean isScraped;
    }
}
