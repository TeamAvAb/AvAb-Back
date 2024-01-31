package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;

public interface FlowRepository extends JpaRepository<Flow, Long> {

    Page<Flow> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Flow> findAllByAuthorOrderByCreatedAtDesc(User user, Pageable pageable);
}
