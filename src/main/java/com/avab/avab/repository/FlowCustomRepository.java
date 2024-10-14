package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;

public interface FlowCustomRepository {
    List<Flow> recommendFlows(
            List<Keyword> keywords,
            Integer participants,
            Integer playTime,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages,
            User user);

    Page<Flow> findScrapFlowsByUser(User user, Pageable pageable);
}
