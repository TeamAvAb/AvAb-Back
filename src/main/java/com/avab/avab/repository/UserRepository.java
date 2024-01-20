package com.avab.avab.repository;

import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);
}
