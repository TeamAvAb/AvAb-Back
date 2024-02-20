package com.avab.avab.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avab.avab.dto.response.RecreationResponseDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationRecommendDTO;
import org.springframework.data.domain.Page;

import com.avab.avab.domain.CustomRecreation;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationAge;
import com.avab.avab.domain.RecreationGender;
import com.avab.avab.domain.RecreationHashtag;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPlace;
import com.avab.avab.domain.RecreationPreparation;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.RecreationWay;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.domain.mapping.RecreationRecreationPurpose;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.CreateRecreationDTO;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.CreateRecreationWayDTO;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationFlowDTO;
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

    public static RecreationFlowDTO toRecreationFlowDTO(FlowRecreation flowRecreation, User user) {
        Recreation recreation = flowRecreation.getRecreation();
        Integer playTime =
                (flowRecreation.getCustomPlayTime() != null)
                        ? flowRecreation.getCustomPlayTime()
                        : recreation.getPlayTime();

        return RecreationFlowDTO.builder()
                .id(recreation.getId())
                .title(recreation.getTitle())
                .totalStars(recreation.getTotalStars())
                .hashtagList(
                        recreation.getRecreationHashTagsList().stream()
                                .map(RecreationHashtag::getHashtag)
                                .toList())
                .keywordList(
                        recreation.getRecreationRecreationKeywordList().stream()
                                .map(
                                        recreationRecreationKeyword ->
                                                recreationRecreationKeyword
                                                        .getKeyword()
                                                        .getKeyword())
                                .toList())
                .playTime(playTime)
                .isCustom(false)
                .build();
    }

    public static RecreationFlowDTO toCustomRecreationFlowDTO(
            FlowRecreation flowRecreation, User user) {
        CustomRecreation customRecreation = flowRecreation.getCustomRecreation();
        return RecreationFlowDTO.builder()
                .id(customRecreation.getId())
                .title(customRecreation.getTitle())
                .keywordList(
                        customRecreation.getRecreationRecreationKeywordList().stream()
                                .map(
                                        customRecreationKeyword ->
                                                customRecreationKeyword.getKeyword().getKeyword())
                                .toList())
                .playTime(flowRecreation.getCustomPlayTime())
                .isCustom(true)
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
                .summary(recreation.getSummary())
                .build();
    }

    public static WayDTO toWayDTO(RecreationWay recreationWay) {
        return WayDTO.builder()
                .contents(recreationWay.getContents())
                .imageUrl(recreationWay.getImageUrl())
                .seq(recreationWay.getSeq())
                .build();
    }

    public static DescriptionDTO toDescriptionDTO(Recreation recreation, User user) {
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

        List<Place> placeList =
                recreation.getRecreationPlaceList().stream()
                        .map(RecreationPlace::getPlace)
                        .collect(Collectors.toList());

        List<Keyword> keywordList =
                recreation.getRecreationRecreationKeywordList().stream()
                        .map(RecreationRecreationKeyword::getKeyword)
                        .map(RecreationKeyword::getKeyword)
                        .collect(Collectors.toList());

        List<Purpose> purposeList =
                recreation.getRecreationRecreationPurposeList().stream()
                        .map(RecreationRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .collect(Collectors.toList());

        return DescriptionDTO.builder()
                .recreationId(recreation.getId())
                .title(recreation.getTitle())
                .summary(recreation.getSummary())
                .minParticipants(recreation.getMinParticipants())
                .maxParticipants(recreation.getMaxParticipants())
                .playTime(recreation.getPlayTime())
                .hashTagList(hashtagList)
                .keywordList(keywordList)
                .purposeList(purposeList)
                .placeList(placeList)
                .genderList(genderList)
                .ageList(ageList)
                .preparationList(preparationList)
                .wayList(wayList)
                .viewCount(recreation.getViewCount())
                .totalStars(recreation.getTotalStars())
                .isFavorite(
                        user != null
                                ? recreation.getRecreationFavoriteList().stream()
                                        .anyMatch(
                                                (recreationFavorite ->
                                                        recreationFavorite.getUser().equals(user)))
                                : null)
                .imageUrl(recreation.getImageUrl())
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
                .totalReviews(reviewPage.getTotalElements())
                .build();
    }

    public static RecreationReviewDTO toRecreationReviewDTO(RecreationReview review, User user) {
        User author = review.getAuthor();
        RecreationReviewRecommendation recommendation =
                review.getRecreationReviewRecommendationList().stream()
                        .filter(rec -> rec.getUser().equals(user))
                        .findFirst()
                        .orElse(null);

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

    public static Recreation toRecreation(
            User user,
            CreateRecreationDTO request,
            String thumbnailImageUrl,
            Map<Integer, String> wayImageUrls,
            List<RecreationKeyword> recreationKeywordList,
            List<RecreationPurpose> recreationPurposeList) {
        Recreation recreation =
                Recreation.builder()
                        .author(user)
                        .title(request.getTitle())
                        .summary(request.getSummary())
                        .playTime(request.getPlayTime())
                        .minParticipants(request.getMinParticipants())
                        .maxParticipants(request.getMaxParticipants())
                        .imageUrl(thumbnailImageUrl)
                        .build();

        List<RecreationHashtag> recreationHashtagList = new ArrayList<>();
        if (request.getHashtags() != null) {
            recreationHashtagList =
                    request.getHashtags().stream()
                            .map(
                                    hashtag ->
                                            RecreationHashtag.builder()
                                                    .recreation(recreation)
                                                    .hashtag(hashtag)
                                                    .build())
                            .toList();
        }

        List<RecreationPlace> recreationPlaceList =
                request.getPlaces().stream()
                        .map(
                                place ->
                                        RecreationPlace.builder()
                                                .recreation(recreation)
                                                .place(place)
                                                .build())
                        .toList();

        List<RecreationPreparation> recreationPreparationList = new ArrayList<>();
        if (request.getPreparations() != null) {
            recreationPreparationList =
                    request.getPreparations().stream()
                            .map(
                                    preparation ->
                                            RecreationPreparation.builder()
                                                    .recreation(recreation)
                                                    .name(preparation)
                                                    .build())
                            .toList();
        }

        List<RecreationAge> recreationAgeList =
                request.getAges().stream()
                        .map(age -> RecreationAge.builder().recreation(recreation).age(age).build())
                        .toList();

        List<RecreationGender> recreationGenderList =
                request.getGenders().stream()
                        .map(
                                gender ->
                                        RecreationGender.builder()
                                                .recreation(recreation)
                                                .gender(gender)
                                                .build())
                        .toList();

        List<RecreationWay> recreationWayList =
                request.getWays().stream()
                        .map(
                                way ->
                                        toRecreationWay(
                                                way, wayImageUrls.get(way.getSeq()), recreation))
                        .toList();

        List<RecreationRecreationKeyword> recreationRecreationKeywordList =
                recreationKeywordList.stream()
                        .map(
                                keyword ->
                                        RecreationRecreationKeyword.builder()
                                                .keyword(keyword)
                                                .recreation(recreation)
                                                .build())
                        .toList();

        List<RecreationRecreationPurpose> recreationRecreationPurposeList =
                recreationPurposeList.stream()
                        .map(
                                purpose ->
                                        RecreationRecreationPurpose.builder()
                                                .purpose(purpose)
                                                .recreation(recreation)
                                                .build())
                        .toList();

        recreation.getRecreationHashTagsList().addAll(recreationHashtagList);
        recreation.getRecreationPlaceList().addAll(recreationPlaceList);
        recreation.getRecreationPreparationList().addAll(recreationPreparationList);
        recreation.getRecreationAgeList().addAll(recreationAgeList);
        recreation.getRecreationGenderList().addAll(recreationGenderList);
        recreation.getRecreationWayList().addAll(recreationWayList);
        recreation.getRecreationRecreationKeywordList().addAll(recreationRecreationKeywordList);
        recreation.getRecreationRecreationPurposeList().addAll(recreationRecreationPurposeList);

        return recreation;
    }

    private static RecreationWay toRecreationWay(
            CreateRecreationWayDTO request, String wayImageUrl, Recreation recreation) {
        return RecreationWay.builder()
                .recreation(recreation)
                .contents(request.getContents())
                .seq(request.getSeq())
                .imageUrl(wayImageUrl)
                .build();
    }

    public static RecreationCreatedDTO toRecreationCreatedDTO(Recreation recreation) {
        return RecreationCreatedDTO.builder().id(recreation.getId()).build();
    }

    public static List<RecreationRecommendDTO> toRecreationRecommendListDTO(
            List<Recreation> recreations, User user) {
        return recreations.stream()
                .map(recreation -> toRecreationRecommendDTO(recreation, user))
                .toList();
    }

    private static RecreationRecommendDTO toRecreationRecommendDTO(Recreation recreation, User user) {
        return RecreationRecommendDTO.builder()
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
                .summary(recreation.getSummary())
                .playTime(recreation.getPlayTime())
                .build();
    }
}
