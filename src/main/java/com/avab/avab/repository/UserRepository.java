package com.avab.avab.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import com.avab.avab.domain.enums.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    Optional<User> findByEmail(String email);

    List<User> findByUserStatusAndDeletedAtBefore(UserStatus userStatus, LocalDate threshold);
}
