package com.avab.avab.repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.mapping.FlowRecreation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowRecreationRepository extends JpaRepository<FlowRecreation, Long> {
    void deleteAllByFlow(Flow flow);
}
