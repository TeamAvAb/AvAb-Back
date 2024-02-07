package com.avab.avab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.enums.Purpose;

public interface RecreationPurposeRepository extends JpaRepository<RecreationPurpose, Integer> {

    Optional<RecreationPurpose> findByPurpose(Purpose purpose);
}
