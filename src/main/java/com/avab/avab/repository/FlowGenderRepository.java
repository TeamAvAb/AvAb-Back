package com.avab.avab.repository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowGender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowGenderRepository extends JpaRepository<FlowGender, Long> {

    void deleteAllByFlow(Flow flow);
}
