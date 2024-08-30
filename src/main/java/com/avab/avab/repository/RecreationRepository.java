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

    Page<Recreation> findByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE Recreation r SET r.viewCount = r.viewCount + :viewCount WHERE r.id = :id")
    void incrementViewCountById(@Param("id") Long id, @Param("viewCount") Long viewCount);

    @Modifying
    @Query(
            "UPDATE Recreation r SET r.weeklyViewCount = r.weeklyViewCount + :viewCount WHERE r.id = :id")
    void incrementWeeklyViewCountById(@Param("id") Long id, @Param("viewCount") Long viewCount);

    @Modifying
    @Query("UPDATE Recreation r SET r.viewCountLast7Days = :viewCount WHERE r.id = :id")
    void updateViewCountLast7DaysById(@Param("id") Long id, @Param("viewCount") Long viewCount);
}
