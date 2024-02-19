package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowAge;

public interface FlowAgeRepository extends JpaRepository<FlowAge, Long> {
    void deleteAllByFlow(Flow flow);
}
