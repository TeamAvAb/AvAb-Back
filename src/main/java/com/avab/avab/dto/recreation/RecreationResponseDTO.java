package com.avab.avab.dto.recreation;

import java.util.List;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecreationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularRecreationListDTO {

        List<Keyword> keywordList;

        List<String> hashtagList;

        String title;

        String imageUrl;

        Float totalStars;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DescriptionDTO {
        Long recreationId;
        String summary;
        List<String> hashTagList;
        List<Age> ageList;
        List<String> preparationList;
        List<WayDTO> wayList;
        List<Gender> genderList;
        Integer minParticipants;
        Integer maxParticipants;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WayDTO {
        String wayList;
        String wayImgList;
    }
}
