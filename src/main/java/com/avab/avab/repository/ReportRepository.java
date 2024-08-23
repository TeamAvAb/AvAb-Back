package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {}
