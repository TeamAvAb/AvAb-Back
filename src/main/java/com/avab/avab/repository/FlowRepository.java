package com.avab.avab.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.UserStatus;

public interface FlowRepository extends JpaRepository<Flow, Long>, FlowCustomRepository {

    Page<Flow> findAllByAuthor_UserStatus(Pageable pageable, UserStatus userStatus);

    Page<Flow> findAllByAuthorOrderByCreatedAtDesc(User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Flow f SET f.viewCount = f.viewCount + :viewCount WHERE f.id = :id")
    void incrementViewCountById(@Param("id") Long id, @Param("viewCount") Long viewCount);

    List<Flow> findAllByIdIn(List<Long> id);

    @Modifying
    @Query("UPDATE Flow f SET f.scrapCount = f.scrapCount + 1 WHERE f.id = :id")
    void incrementScrapCountById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Flow f SET f.scrapCount = f.scrapCount - 1 WHERE f.id = :id")
    void decrementScrapCountById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Flow f SET f.viewCountLast7Days = :viewCount WHERE f.id = :id")
    void updateViewCountLast7DaysById(@Param("id") Long id, @Param("viewCount") Long viewCount);
}
