package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewListDTO;

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
                .totalStars(recreation.getTotalStars())
                .build();
    }

    public static RecreationPreviewListDTO toRecreationPreviewListDTO(
            Page<Recreation> recreationPage, User user) {
        return RecreationPreviewListDTO.builder()
                .recreationList(
                        recreationPage.getContent().stream()
                                .map(recreation -> toRecreationPreviewDTO(recreation, user))
                                .toList())
                .totalPages(recreationPage.getTotalPages())
                .build();
    }

    private static RecreationPreviewDTO toRecreationPreviewDTO(Recreation recreation, User user) {
        return RecreationPreviewDTO.builder()
                .id(recreation.getId())
                .hashtagList(
                        recreation.getRecreationHashTagsList().stream()
                                .map(RecreationHashtag::getHashtag)
                                .toList())
                .isFavorite(
                        user != null
                                ? recreation.getRecreationFavoriteList().stream()
                                        .anyMatch(
                                                (recreationFavorite ->
                                                        recreationFavorite.getUser().equals(user)))
                                : null)
                .imageUrl(recreation.getImageUrl())
                .keywordList(
                        recreation.getRecreationRecreationKeywordList().stream()
                                .map(
                                        recreationRecreationKeyword ->
                                                recreationRecreationKeyword
                                                        .getKeyword()
                                                        .getKeyword())
                                .toList())
                .title(recreation.getTitle())
                .totalStars(recreation.getTotalStars())
                .build();
    }
}
