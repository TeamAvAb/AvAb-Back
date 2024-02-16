package com.avab.avab.repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowRecreationPurposeRepository extends JpaRepository<FlowRecreationPurpose, Long> {

    void deleteAllByFlow(Flow flow);
}
