package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowScrap;

public interface FlowScrapRepository extends JpaRepository<FlowScrap, Long> {

    Optional<FlowScrap> findByFlowAndUser(Flow flow, User user);
}
