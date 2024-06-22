package com.avab.avab.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.avab.avab.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("SELECT u FROM User u WHERE u.userStatus = 'DELETED' AND u.deletedTime <= :threshold")
    Optional<List<User>> findOldUsers(@Param("threshold") LocalDate threshold);
}
