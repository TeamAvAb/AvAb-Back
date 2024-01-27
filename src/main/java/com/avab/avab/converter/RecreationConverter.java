package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationAge;
import com.avab.avab.domain.RecreationGender;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationPreparation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.RecreationWay;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.WayDTO;
import com.avab.avab.dto.response.RecreationReviewResponseDTO.RecommendationDTO;

public class RecreationConverter {

    public static RecreationPreviewPageDTO toRecreationPreviewPageDTO(
            Page<Recreation> recreationPage, User user) {
        return RecreationPreviewPageDTO.builder()
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
                .viewCount(recreation.getViewCount())
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
            Page<RecreationReview> reviewPage, User user) {
        return RecreationReviewPageDTO.builder()
                .reviewList(
                        reviewPage.stream()
                                .map(recreation -> toRecreationReviewDTO(recreation, user))
                                .toList())
                .totalPages(reviewPage.getTotalPages())
                .build();
    }

    public static RecreationReviewDTO toRecreationReviewDTO(RecreationReview review, User user) {
        User author = review.getAuthor();
        RecreationReviewRecommendation recommendation =
                review.getRecreationReviewRecommendationList().stream()
                        .filter(rec -> rec.getUser().equals(user))
                        .findFirst()
                        .orElseGet(() -> null);

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
                .recommendation(
                        user != null
                                ? RecommendationDTO.builder()
                                        .isRecommended(recommendation != null)
                                        .type(
                                                recommendation != null
                                                        ? recommendation.getType()
                                                        : null)
                                        .build()
                                : null)
                .build();
    }

    public static List<RecreationPreviewDTO> toRecreationPreviewListDTO(
            List<Recreation> recreations, User user) {
        return recreations.stream()
                .map(recreation -> toRecreationPreviewDTO(recreation, user))
                .toList();
    }
}
