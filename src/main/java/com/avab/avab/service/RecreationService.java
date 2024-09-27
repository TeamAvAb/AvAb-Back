package com.avab.avab.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.avab.avab.controller.enums.SortCondition;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.CreateRecreationDTO;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;

public interface RecreationService {

    Page<Recreation> getTop9RecreationsByWeeklyViewCount(User user);

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
            Integer page,
            SortCondition sortCondition);

    Recreation getRecreationDescription(Long recreationId, User user);

    Boolean toggleFavoriteRecreation(Long recreationId, User user);

    RecreationReview createReview(User user, Long recreationId, PostRecreationReviewDTO request);

    Page<RecreationReview> getRecreationReviews(Long recreationId, User user, Integer page);

    List<Recreation> findRelatedRecreations(User user, Long recreationId);

    List<Flow> findRelatedFlows(Long recreationId, User user);

    Recreation createRecreation(
            User user,
            CreateRecreationDTO request,
            MultipartFile thumbnailImage,
            List<MultipartFile> wayImages);

    List<Recreation> recommendRecreations(
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            User user);

    Page<Recreation> getRecentRecreation(Integer page);

    void incrementViewCountLast7DaysById(Long flowId, Long viewCount);

    void incrementViewCountById(Long id, Long viewCount);
}
