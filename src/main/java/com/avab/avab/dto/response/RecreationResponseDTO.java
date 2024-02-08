package com.avab.avab.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.response.RecreationReviewResponseDTO.RecommendationDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecreationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationPreviewDTO {

        Long id;
        List<String> hashtagList;
        String title;
        Float totalStars;
        List<Keyword> keywordList;
        String imageUrl;
        String summary;

        @Schema(description = "즐겨찾기 여부, 미로그인시 null")
        Boolean isFavorite;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationPreviewPageDTO {

        List<RecreationPreviewDTO> recreationList;
        Integer totalPages;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DescriptionDTO {

        Long recreationId;
        String title;
        String summary;
        Integer playTime;
        List<String> hashTagList;
        List<Keyword> keywordList;
        List<Purpose> purposeList;
        List<Place> placeList;
        List<Age> ageList;
        List<String> preparationList;
        List<WayDTO> wayList;
        List<Gender> genderList;
        Integer minParticipants;
        Integer maxParticipants;
        Long viewCount;
        Float totalStars;

        @Schema(description = "즐겨찾기 여부, 미로그인시 null")
        Boolean isFavorite;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class WayDTO {

        String contents;
        String imageUrl;
        Integer seq;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FavoriteDTO {

        Boolean isFavorite;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationReviewCreatedDTO {

        Long reviewId;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationReviewDTO {

        Long reviewId;
        AuthorDTO author;
        Integer stars;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        String contents;
        Integer goodCount;
        Integer badCount;

        @Schema(description = "로그인하지 않은 경우 null", nullable = true)
        RecommendationDTO recommendation;

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class AuthorDTO {

            Long userId;
            String username;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationReviewPageDTO {

        List<RecreationReviewDTO> reviewList;
        Integer totalPages;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecreationCreatedDTO {

        Long id;
    }
}
