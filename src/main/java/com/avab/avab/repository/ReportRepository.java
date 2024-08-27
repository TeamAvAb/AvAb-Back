package com.avab.avab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Boolean existsByReporterAndTargetRecreation(User reporter, Recreation targetRecreation);

    Boolean existsByReporterAndTargetFlow(User reporter, Flow targetFlow);
}
