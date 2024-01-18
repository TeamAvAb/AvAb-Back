package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;

public interface RecreationCustomRepository {

    Page<Recreation> searchRecreations(
            String searchKeyword,
            Keyword keyword,
            Integer participants,
            Integer playTime,
            Place place,
            Gender gender,
            Age age,
            Pageable page);
}
