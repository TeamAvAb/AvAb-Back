package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.enums.UserStatus;

public interface RecreationRepository
        extends JpaRepository<Recreation, Long>, RecreationCustomRepository {

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

    Optional<Recreation> findByIdAndDeletedAtIsNullAndAuthor_UserStatusNot(
            Long id, UserStatus userStatus);
}
