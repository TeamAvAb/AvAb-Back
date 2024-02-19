package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;

public interface FlowRecreationPurposeRepository
        extends JpaRepository<FlowRecreationPurpose, Long> {

    void deleteAllByFlow(Flow flow);
}
