package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;

public interface RecreationCustomRepository {

    Page<Recreation> searchRecreations(
            String searchKeyword,
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Place> places,
            List<Gender> genders,
            List<Age> ages,
            Pageable page);

    List<Recreation> relatedRecreations(
            Long recreationId,
            List<Keyword> keyword,
            List<Purpose> purpose,
            Integer maxParticipants,
            List<Age> age);
}
