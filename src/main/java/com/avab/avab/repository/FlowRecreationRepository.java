package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.mapping.FlowRecreation;

public interface FlowRecreationRepository extends JpaRepository<FlowRecreation, Long> {
    void deleteAllByFlow(Flow flow);
}
