package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;

public interface FlowRecreationKeywordRepository
        extends JpaRepository<FlowRecreationKeyword, Long> {
    void deleteAllByFlow(Flow flow);
}
