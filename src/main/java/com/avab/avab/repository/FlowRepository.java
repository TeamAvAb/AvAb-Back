package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;

public interface FlowRepository extends JpaRepository<Flow, Long> {

    Page<Flow> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
