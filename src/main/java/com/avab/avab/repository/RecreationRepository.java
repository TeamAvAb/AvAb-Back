package com.avab.avab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avab.avab.domain.Recreation;

public interface RecreationRepository
        extends JpaRepository<Recreation, Long>, RecreationCustomRepository {

    @Query("SELECT r FROM Recreation r ORDER BY r.weeklyViewCount DESC")
    Page<Recreation> findTop9ByOrderByWeeklyViewCount(Pageable pageable);

    @Modifying
    @Query("UPDATE Recreation r SET r.viewCount = r.viewCount + :viewCount WHERE r.id = :id")
    void incrementViewCountById(@Param("id") Long id, @Param("viewCount") Long viewCount);
}
