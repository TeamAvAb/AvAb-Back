package com.avab.avab.repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowAge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowAgeRepository extends JpaRepository<FlowAge, Long> {
    void deleteAllByFlow(Flow flow);
}
