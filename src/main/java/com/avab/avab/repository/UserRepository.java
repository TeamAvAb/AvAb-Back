package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
