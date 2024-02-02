package com.avab.avab.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.converter.RecreationConverter;
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
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.redis.service.RecreationViewCountService;
import com.avab.avab.repository.RecreationFavoriteRepository;
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

        recreationViewCountService.incrementViewCount(recreation.getId());

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
        return recreationReviewRepository.save(review);
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
            Integer page) {
        if (!isAtLeastOneConditionNotNull(
                searchKeyword, keywords, participants, playTime, places, purposes, genders, ages)) {
            throw new RecreationException(ErrorStatus.SEARCH_CONDITION_INVALID);
        }

        return recreationRepository.searchRecreations(
                searchKeyword,
                keywords,
                participants,
                playTime,
                places,
                purposes,
                genders,
                ages,
                PageRequest.of(page, SEARCH_PAGE_SIZE));
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
    public List<Recreation> recommendRecreations(List<Keyword> keywords, Integer participants, Integer playTime, List<Purpose> purposes, List<Gender> genders, List<Age> ages) {
        return recreationRepository.recommendRecreations(purposes, keywords, genders, ages, participants, playTime);
    }


}
