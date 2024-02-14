package com.avab.avab.dto.response;

import java.util.List;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationFlowDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowDetailPageDTO {
        FlowDetailDTO flowDetail;
        List<RecreationFlowDTO> recreations;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowDetailDTO {

        Long id;
        Integer totalPlayTime;
        Integer participants;
        Long viewCount;
        String title;
        List<Age> age;
        List<Keyword> keywordList;
        List<Gender> gender;
        List<Purpose> purposeList;
        AuthorDTO author;
        Integer scrapCount;

        @Schema(description = "즐겨찾기 여부, 미로그인시 null")
        Boolean isFavorite;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowScrapDTO {

        Boolean isScraped;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeletedFlowDTO {

        Long flowId;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FlowCreatedDTO {

        Long flowId;
    }
}
