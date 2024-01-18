package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationAge;
import com.avab.avab.domain.RecreationPreparation;
import com.avab.avab.domain.RecreationWay;
import com.avab.avab.domain.RecreationGender;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.dto.recreation.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.recreation.RecreationResponseDTO.PopularRecreationListDTO;

public class RecreationConverter {

    public static PopularRecreationListDTO convertToDTO(Recreation recreation) {
        // 키워드 리스트 추출 및 변환
        List<Keyword> keywordList =
                recreation.getRecreationRecreationKeywordList().stream()
                        .map(RecreationRecreationKeyword::getKeyword)
                        .map(RecreationKeyword::getKeyword)
                        .collect(Collectors.toList());

        // 해시태그 리스트 추출 및 변환
        List<String> hashtagList =
                recreation.getRecreationHashTagsList().stream()
                        .map(RecreationHashtag::getHashtag)
                        .collect(Collectors.toList());

        return PopularRecreationListDTO.builder()
                .keywordList(keywordList)
                .hashtagList(hashtagList)
                .title(recreation.getTitle())
                .imageUrl(recreation.getImageUrl())
                .totalStars(recreation.getTotal_stars())
                .build();
    }

    public static DescriptionDTO toDescriptionDTO(Recreation recreation) {
        List<String> hashtagList =
                recreation.getRecreationHashTagsList().stream()
                        .map(RecreationHashtag::getHashtag)
                        .collect(Collectors.toList());

        List<Age> ageList =
                recreation.getRecreationAgeList().stream()
                        .map(RecreationAge::getAge)
                        .collect(Collectors.toList());

        List<String> preparationList =
                recreation.getRecreationPreparationList().stream()
                        .map(RecreationPreparation::getName)
                        .collect(Collectors.toList());

        List<Gender> genderList =
                recreation.getRecreationGenderList().stream()
                        .map(RecreationGender::getGender)
                        .collect(Collectors.toList());

        List<String> wayList =
                recreation.getRecreationWayList().stream()
                        .map(RecreationWay::getContents)
                        .collect(Collectors.toList());

        List<String> wayImgList =
                recreation.getRecreationWayList().stream()
                        .map(RecreationWay::getImageUrl)
                        .collect(Collectors.toList());

        return DescriptionDTO.builder()
                .recreationId(recreation.getId())
                .summary(recreation.getSummary())
                .minParticipants(recreation.getMinParticipants())
                .maxParticipants(recreation.getMaxParticipants())
                .hashTagList(hashtagList)
                .genderList(genderList)
                .ageList(ageList)
                .preparationList(preparationList)
                .wayList(wayList)
                .wayImgList(wayImgList)
                .build();
    }
}
