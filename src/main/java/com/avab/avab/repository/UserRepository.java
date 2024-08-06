package com.avab.avab.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    Optional<User> findByEmail(String email);

    // SQLRestriction를 무시하기 위해 native query로 짬.
    @Query(
            value =
                    "SELECT * FROM user WHERE user_status = 'DELETED' AND deleted_time <= :threshold",
            nativeQuery = true)
    Optional<List<User>> findOldUsers(@Param("threshold") LocalDate threshold);
}
