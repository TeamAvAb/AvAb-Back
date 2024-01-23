package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationAge;
import com.avab.avab.domain.RecreationGender;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPreparation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.RecreationWay;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewListDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.WayDTO;

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

    public static WayDTO toWayDTO(RecreationWay recreationWay) {
        return WayDTO.builder()
                .contents(recreationWay.getContents())
                .imageUrl(recreationWay.getImageUrl())
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

        List<WayDTO> wayList =
                recreation.getRecreationWayList().stream()
                        .map(RecreationConverter::toWayDTO)
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
                .build();
    }

    public static RecreationFavorite toRecreationFavorite(Recreation recreation, User user) {
        return RecreationFavorite.builder().recreation(recreation).user(user).build();
    }

    public static FavoriteDTO toFavoriteDTO(Boolean isFavorite) {
        return FavoriteDTO.builder().isFavorite(isFavorite).build();
    }

    public static RecreationReview toRecreationReview(
            User user, Recreation recreation, PostRecreationReviewDTO request) {
        return RecreationReview.builder()
                .recreation(recreation)
                .author(user)
                .contents(request.getContents())
                .stars(request.getStars())
                .build();
    }

    public static RecreationReviewCreatedDTO toRecreationReviewCreatedDTO(RecreationReview review) {
        return RecreationReviewCreatedDTO.builder().reviewId(review.getId()).build();
    }

    public static RecreationReviewPageDTO toRecreationReviewPageDTO(
            Page<RecreationReview> reviewPage) {
        return RecreationReviewPageDTO.builder()
                .reviewList(
                        reviewPage.stream()
                                .map(RecreationConverter::toRecreationReviewDTO)
                                .toList())
                .totalPages(reviewPage.getTotalPages())
                .build();
    }

    public static RecreationReviewDTO toRecreationReviewDTO(RecreationReview review) {
        User author = review.getAuthor();

        return RecreationReviewDTO.builder()
                .reviewId(review.getId())
                .stars(review.getStars())
                .author(
                        AuthorDTO.builder()
                                .userId(author.getId())
                                .username(author.getUsername())
                                .build())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .contents(review.getContents())
                .goodCount(review.getGoodCount())
                .badCount(review.getBadCount())
                .build();
    }
}
