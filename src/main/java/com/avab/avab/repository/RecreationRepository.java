package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.avab.avab.domain.Recreation;

public interface RecreationRepository extends JpaRepository<Recreation, Long> {

    @Query("SELECT r FROM Recreation r ORDER BY r.viewCount DESC")
    List<Recreation> findTop3ByOrderByViewCountDesc(Pageable pageable);
}
