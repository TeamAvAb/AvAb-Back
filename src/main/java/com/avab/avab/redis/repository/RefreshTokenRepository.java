package com.avab.avab.redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.avab.avab.redis.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {}
