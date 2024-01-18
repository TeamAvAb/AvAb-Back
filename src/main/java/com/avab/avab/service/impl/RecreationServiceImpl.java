package com.avab.avab.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.service.RecreationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecreationServiceImpl implements RecreationService {

    private final RecreationRepository recreationRepository;
    private final Integer SEARCH_PAGE_SIZE = 9;

    public List<PopularRecreationListDTO> getTop3RecreationsByViewCount() {
        List<Recreation> topRecreations =
                recreationRepository.findTop3ByOrderByViewCountDesc(PageRequest.of(0, 3));
        return topRecreations.stream()
                .map(RecreationConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Recreation> searchRecreations(
            User user,
            String searchKeyword,
            Keyword keyword,
            Integer participants,
            Integer playTime,
            Place place,
            Gender gender,
            Age age,
            Integer page) {
        if (!isAtLeastOneConditionNotNull(
                searchKeyword, keyword, participants, playTime, place, gender, age)) {
            throw new RecreationException(ErrorStatus.SEARCH_CONDITION_INVALID);
        }

        return recreationRepository.searchRecreations(
                searchKeyword,
                keyword,
                participants,
                playTime,
                place,
                gender,
                age,
                PageRequest.of(page, SEARCH_PAGE_SIZE));
    }

    private Boolean isAtLeastOneConditionNotNull(
            String searchKeyword,
            Keyword keyword,
            Integer participants,
            Integer playTime,
            Place place,
            Gender gender,
            Age age) {
        return searchKeyword != null
                || keyword != null
                || participants != null
                || playTime != null
                || place != null
                || gender != null
                || age != null;
    }
}
