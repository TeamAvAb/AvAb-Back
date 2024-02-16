package com.avab.avab.repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowRecreationKeywordRepository extends JpaRepository<FlowRecreationKeyword, Long> {
    void deleteAllByFlow(Flow flow);
}
