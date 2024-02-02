package com.avab.avab.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;

public interface RecreationService {

    Page<Recreation> getTop9RecreationsByWeeklyViewCount();

    Page<Recreation> searchRecreations(
            User user,
            String searchKeyword,
            List<Keyword> keyword,
            Integer participants,
            Integer playTime,
            List<Place> place,
            List<Purpose> purposes,
            List<Gender> gender,
            List<Age> age,
            Integer page);

    Recreation getRecreationDescription(Long recreationId);

    Boolean toggleFavoriteRecreation(Long recreationId, User user);

    RecreationReview createReview(User user, Long recreationId, PostRecreationReviewDTO request);

    Page<RecreationReview> getRecreationReviews(Long recreationId, Integer page);

    List<Recreation> findRelatedRecreations(User user, Long recreationId);

    List<Recreation> recommendRecreations(List<Keyword> keywords, Integer participants, Integer playTime, List<Purpose> purposes, List<Gender> genders, List<Age> ages);
}
