package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationFavorite;

public interface RecreationFavoriteRepository extends JpaRepository<RecreationFavorite, Long> {

    Optional<RecreationFavorite> findByRecreationAndUser(Recreation recreation, User user);
}
