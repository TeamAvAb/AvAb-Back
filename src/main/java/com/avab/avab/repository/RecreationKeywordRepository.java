package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.enums.Keyword;

public interface RecreationKeywordRepository extends JpaRepository<RecreationKeyword, Long> {

    RecreationKeyword findByKeyword(Keyword keyword);
}
