package com.avab.avab.converter;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.ReportType;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportFlowRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationReviewDTO;
import com.avab.avab.dto.response.ReportResponseDTO.ReportCreatedResponseDTO;

public class ReportConverter {

    public static ReportCreatedResponseDTO toReportCreatedResponseDTO(Report report) {
        return ReportCreatedResponseDTO.builder().reportId(report.getId()).build();
    }

    public static Report toReport(
            User reporter, ReportRecreationRequestDTO request, Recreation targetRecreation) {
        return Report.builder()
                .reporter(reporter)
                .reportType(ReportType.RECREATION)
                .targetRecreation(targetRecreation)
                .reason(request.getReason())
                .extraReason(request.getExtraReason())
                .build();
    }

    public static Report toReport(User reporter, ReportFlowRequestDTO request, Flow targetFlow) {
        return Report.builder()
                .reporter(reporter)
                .reportType(ReportType.FLOW)
                .targetFlow(targetFlow)
                .reason(request.getReason())
                .extraReason(request.getExtraReason())
                .build();
    }

    public static Report toReport(
            User reporter,
            ReportRecreationReviewDTO request,
            RecreationReview targetRecreationReview) {
        return Report.builder()
                .reporter(reporter)
                .reportType(ReportType.RECREATION_REVIEW)
                .targetRecreationReview(targetRecreationReview)
                .reason(request.getReason())
                .extraReason(request.getExtraReason())
                .build();
    }
}
