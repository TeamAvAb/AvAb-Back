package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
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
}
