package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;

public interface FlowFavoriteRepository extends JpaRepository<FlowFavorite, Long> {
    Page<FlowFavorite> findByUser(User user, Pageable pageable);
}
