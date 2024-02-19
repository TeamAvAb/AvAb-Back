package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowGender;

public interface FlowGenderRepository extends JpaRepository<FlowGender, Long> {

    void deleteAllByFlow(Flow flow);
}
