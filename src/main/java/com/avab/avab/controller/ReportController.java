package com.avab.avab.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.apiPayload.code.status.SuccessStatus;
import com.avab.avab.converter.ReportConverter;
import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportFlowRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationReviewDTO;
import com.avab.avab.dto.response.ReportResponseDTO.ReportCreatedResponseDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
@Tag(name = "Report 📣", description = "신고 관련 API")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "레크레이션 신고 API", description = "레크레이션을 신고합니다 _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "신고 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/recreations")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<ReportCreatedResponseDTO> reportRecreation(
            @AuthUser User user, @RequestBody @Valid ReportRecreationRequestDTO request) {

        Report report = reportService.reportRecreation(user, request);

        return BaseResponse.of(
                SuccessStatus._CREATED, ReportConverter.toReportCreatedResponseDTO(report));
    }

    @Operation(summary = "플로우 신고 API", description = "플로우를 신고합니다 _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "신고 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/flows")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<ReportCreatedResponseDTO> reportFlow(
            @AuthUser User user, @RequestBody @Valid ReportFlowRequestDTO request) {

        Report report = reportService.reportFlow(user, request);

        return BaseResponse.of(
                SuccessStatus._CREATED, ReportConverter.toReportCreatedResponseDTO(report));
    }

    @Operation(summary = "레크레이션 리뷰 신고 API", description = "레크레이션 리뷰를 신고합니다 _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "신고 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/recreation-reviews")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<ReportCreatedResponseDTO> reportRecreationReview(
            @AuthUser User user, @RequestBody @Valid ReportRecreationReviewDTO request) {

        Report report = reportService.reportRecreationReview(user, request);

        return BaseResponse.of(
                SuccessStatus._CREATED, ReportConverter.toReportCreatedResponseDTO(report));
    }
}
