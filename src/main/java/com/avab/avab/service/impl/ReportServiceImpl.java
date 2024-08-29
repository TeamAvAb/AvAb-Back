package com.avab.avab.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.FlowException;
import com.avab.avab.apiPayload.exception.RecreationException;
import com.avab.avab.converter.ReportConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportFlowRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationReviewDTO;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.repository.RecreationReviewRepository;
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
    private final FlowRepository flowRepository;
    private final RecreationReviewRepository recreationReviewRepository;

    @Override
    @Transactional
    public Report reportRecreation(User user, ReportRecreationRequestDTO request) {
        Recreation targetRecreation =
                recreationRepository
                        .findById(request.getRecreationId())
                        .orElseThrow(
                                () -> new RecreationException(ErrorStatus.RECREATION_NOT_FOUND));

        if (reportRepository.existsByReporterAndTargetRecreation(user, targetRecreation)) {
            throw new RecreationException(ErrorStatus.ALREADY_REPORTED);
        }

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

    @Override
    @Transactional
    public Report reportFlow(User user, ReportFlowRequestDTO request) {
        Flow targetFlow =
                flowRepository
                        .findById(request.getFlowId())
                        .orElseThrow(() -> new FlowException(ErrorStatus.FLOW_NOT_FOUND));

        if (reportRepository.existsByReporterAndTargetFlow(user, targetFlow)) {
            throw new RecreationException(ErrorStatus.ALREADY_REPORTED);
        }

        Report report = ReportConverter.toReport(user, request, targetFlow);
        reportRepository.save(report);

        if (targetFlow.getReportCount().equals(SOFT_DELETE_THRESHOLD)) {
            targetFlow.softDelete();
        }

        User author = targetFlow.getAuthor();
        author.incrementReportCount();
        if (author.getReportCount().equals(USER_DISABLE_THRESHOLD)) {
            author.disableUser();
        }

        return report;
    }

    @Override
    @Transactional
    public Report reportRecreationReview(User user, ReportRecreationReviewDTO request) {
        RecreationReview targetRecreationReview =
                recreationReviewRepository
                        .findById(request.getRecreationReviewId())
                        .orElseThrow(() -> new RecreationException(ErrorStatus.REVIEW_NOT_FOUND));

        if (reportRepository.existsByReporterAndTargetRecreationReview(
                user, targetRecreationReview)) {
            throw new RecreationException(ErrorStatus.ALREADY_REPORTED);
        }

        Report report = ReportConverter.toReport(user, request, targetRecreationReview);
        reportRepository.save(report);

        if (targetRecreationReview.getReportCount().equals(SOFT_DELETE_THRESHOLD)) {
            targetRecreationReview.softDelete();
        }

        User author = targetRecreationReview.getAuthor();
        author.incrementReportCount();
        if (author.getReportCount().equals(USER_DISABLE_THRESHOLD)) {
            author.disableUser();
        }

        return report;
    }
}
