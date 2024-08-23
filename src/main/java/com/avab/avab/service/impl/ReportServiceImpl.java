package com.avab.avab.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.converter.ReportConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.repository.ReportRepository;
import com.avab.avab.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final Integer SOFT_DELETE_THRESHOLD = 3;
    private final Integer USER_DISABLE_THRESHOLD = 10;
    private final ReportRepository reportRepository;
    private final RecreationRepository recreationRepository;

    @Override
    @Transactional
    public Report reportRecreation(User user, ReportRecreationRequestDTO request) {
        Recreation targetRecreation =
                recreationRepository
                        .findById(request.getRecreationId())
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));
        Report report = ReportConverter.toReport(user, request, targetRecreation);
        reportRepository.save(report);

        if (targetRecreation.getReportList().size() == SOFT_DELETE_THRESHOLD) {
            targetRecreation.softDelete();
        }

        User author = targetRecreation.getAuthor();
        author.incrementReportCount();
        if (author.getReportCount().equals(USER_DISABLE_THRESHOLD)) {
            author.disableUser();
        }

        return report;
    }
}
