package com.avab.avab.repository;

import com.avab.avab.domain.mapping.FlowRecreation;
import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.CustomRecreation;

public interface CustomRecreationRepository extends JpaRepository<CustomRecreation, Long> {
}
