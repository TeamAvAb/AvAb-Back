package com.avab.avab.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.FlowException;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.apiPayload.exception.S3Exception;
import com.avab.avab.aws.s3.AmazonS3Manager;
import com.avab.avab.controller.enums.SortCondition;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationAge;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import com.avab.avab.domain.mapping.RecreationRecreationPurpose;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.CreateRecreationDTO;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.redis.service.RecreationViewCountService;
import com.avab.avab.repository.RecreationFavoriteRepository;
import com.avab.avab.repository.RecreationKeywordRepository;
import com.avab.avab.repository.RecreationPurposeRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.repository.RecreationReviewRepository;
import com.avab.avab.service.RecreationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecreationServiceImpl implements RecreationService {

    private final RecreationRepository recreationRepository;
    private final RecreationFavoriteRepository recreationFavoriteRepository;
    private final RecreationReviewRepository recreationReviewRepository;
    private final RecreationViewCountService recreationViewCountService;
    private final RecreationKeywordRepository recreationKeywordRepository;
    private final RecreationPurposeRepository recreationPurposeRepository;
    private final AmazonS3Manager s3Manager;
    private final Integer SEARCH_PAGE_SIZE = 9;
    private final Integer REVIEW_PAGE_SIZE = 2;

    public Page<Recreation> getTop9RecreationsByWeeklyViewCount() {
        return recreationRepository.findTop9ByOrderByWeeklyViewCountDesc(PageRequest.of(0, 9));
    }

    @Transactional
    public Recreation getRecreationDescription(Long recreationId) {
        Recreation recreation =
                recreationRepository
                        .findById(recreationId)
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));

        recreationViewCountService.incrementViewCount(recreationId);
        recreationViewCountService.incrementViewCountLast7Days(recreationId);

        return recreation;
    }

    @Override
    @Transactional
    public Boolean toggleFavoriteRecreation(Long recreationId, User user) {
        Recreation recreation =
                recreationRepository
                        .findById(recreationId)
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));
        Optional<RecreationFavorite> recreationFavorite =
                recreationFavoriteRepository.findByRecreationAndUser(recreation, user);

        if (recreationFavorite.isPresent()) {
            recreationFavoriteRepository.delete(recreationFavorite.get());

            return false;
        } else {
            RecreationFavorite favorite =
                    RecreationConverter.toRecreationFavorite(recreation, user);
            recreationFavoriteRepository.save(favorite);

            return true;
        }
    }

    @Override
    @Transactional
    public RecreationReview createReview(
            User user, Long recreationId, PostRecreationReviewDTO request) {
        Recreation recreation =
                recreationRepository
                        .findById(recreationId)
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));

        RecreationReview review = RecreationConverter.toRecreationReview(user, recreation, request);
        recreationReviewRepository.save(review);

        recreationRepository.updateTotalStars(recreation.getId());
        return review;
    }

    @Override
    public Page<RecreationReview> getRecreationReviews(Long recreationId, Integer page) {
        Recreation recreation =
                recreationRepository
                        .findById(recreationId)
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));

        return recreationReviewRepository.findByRecreation(
                recreation, PageRequest.of(page, REVIEW_PAGE_SIZE));
    }

    @Override
    public Page<Recreation> searchRecreations(
            User user,
            String searchKeyword,
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Place> places,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            Integer page,
            SortCondition sortCondition) {
        if (!isAtLeastOneConditionNotNull(
                searchKeyword, keywords, participants, playTime, places, purposes, genders, ages)) {
            throw new RecreationException(ErrorStatus.SEARCH_CONDITION_INVALID);
        }

        Sort.TypedSort<Recreation> recreationSort = Sort.sort(Recreation.class);
        Sort sort;
        switch (sortCondition) {
            case VIEW -> sort = recreationSort.by(Recreation::getViewCountLast7Days).descending();
            case RECENT -> sort = recreationSort.by(Recreation::getCreatedAt).descending();
            case LIKE -> sort = recreationSort.by(Recreation::getTotalStars).descending();
            default -> throw new RecreationException(ErrorStatus.INVALID_SORT_CONDITION);
        }

        return recreationRepository.searchRecreations(
                user,
                searchKeyword,
                keywords,
                participants,
                playTime,
                places,
                purposes,
                genders,
                ages,
                PageRequest.of(page, SEARCH_PAGE_SIZE, sort));
    }

    private Boolean isAtLeastOneConditionNotNull(
            String searchKeyword,
            List<Keyword> keyword,
            Integer participants,
            Integer playTime,
            List<Place> place,
            List<Purpose> purposes,
            List<Gender> gender,
            List<Age> age) {
        return searchKeyword != null
                || keyword != null
                || participants != null
                || playTime != null
                || place != null
                || purposes != null
                || gender != null
                || age != null;
    }

    public List<Recreation> findRelatedRecreations(User user, Long recreationId) {
        Recreation recreation =
                recreationRepository
                        .findById(recreationId)
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));

        return recreationRepository.findRelatedRecreations(
                recreationId,
                recreation.getRecreationRecreationKeywordList().stream()
                        .map(RecreationRecreationKeyword::getKeyword)
                        .map(RecreationKeyword::getKeyword)
                        .collect(Collectors.toList()),
                recreation.getRecreationRecreationPurposeList().stream()
                        .map(RecreationRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .collect(Collectors.toList()),
                recreation.getMaxParticipants(),
                recreation.getRecreationAgeList().stream()
                        .map(RecreationAge::getAge)
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public Recreation createRecreation(
            User user,
            CreateRecreationDTO request,
            MultipartFile thumbnailImage,
            List<MultipartFile> wayImages) {
        String thumbnailImageUrl = null;
        if (thumbnailImage != null) {
            thumbnailImageUrl = s3Manager.uploadRecreationThumbnailImage(thumbnailImage);
        }

        Map<Integer, String> wayImageUrls = new HashMap<>();
        if (wayImages != null) {
            wayImages.forEach(
                    image -> {
                        try {
                            Integer order =
                                    Integer.parseInt(image.getOriginalFilename().split("_")[0]);
                            String imageUrl = s3Manager.uploadRecreationWayImage(image);
                            wayImageUrls.put(order, imageUrl);
                        } catch (Exception e) {
                            throw new S3Exception(ErrorStatus.S3_UPLOAD_FAIL);
                        }
                    });
        }

        List<RecreationKeyword> recreationKeywordList =
                request.getKeywords().stream()
                        .map(keyword -> recreationKeywordRepository.findByKeyword(keyword).get())
                        .toList();

        List<RecreationPurpose> recreationPurposeList =
                request.getPurposes().stream()
                        .map(purpose -> recreationPurposeRepository.findByPurpose(purpose).get())
                        .toList();

        Recreation recreation =
                RecreationConverter.toRecreation(
                        user,
                        request,
                        thumbnailImageUrl,
                        wayImageUrls,
                        recreationKeywordList,
                        recreationPurposeList);

        return recreationRepository.save(recreation);
    }

    @Override
    public Page<Recreation> getRecentRecreation(Integer page) {
        return recreationRepository.findByOrderByCreatedAtDesc(
                PageRequest.of(page, SEARCH_PAGE_SIZE));
    }

    @Override
    public List<Flow> findRelatedFlows(Long recreationId, User user) {
        return recreationRepository.findRelatedFlows(recreationId, user);
    }

    @Override
    public List<Recreation> recommendRecreations(
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages) {
        return recreationRepository.recommendRecreations(
                purposes, keywords, genders, ages, participants, playTime);
    }

    @Override
    @Transactional
    public void updateFlowViewCountLast7Days(Long flowId, Long viewCount) {
        recreationRepository.updateViewCountLast7DaysById(flowId, viewCount);
    }
}
