package com.avab.avab.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.apiPayload.code.status.SuccessStatus;
import com.avab.avab.converter.RecreationReviewConverter;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationReviewRecommendation;
import com.avab.avab.dto.reqeust.RecreationReviewRequestDTO.ToggleRecommendationDTO;
import com.avab.avab.dto.response.RecreationReviewResponseDTO.RecommendationDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.RecreationReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recreation-reviews")
@RequiredArgsConstructor
@Validated
@Tag(name = "Recreation Review 💬", description = "레크레이션 리뷰 관련 API")
public class RecreationReviewController {

    private final RecreationReviewService recreationReviewService;

    @Operation(
            summary = "레크레이션 리뷰 추천 토글 API",
            description =
                    "리뷰를 추천합니다.\n\n"
                            + "1.처음 추천한 경우: 새 추천을 생성\n\n"
                            + "2.추천했다가 비추한 경우: 비추로 수정 (반대도 가능)\n\n"
                            + "3.추천했다가 또 추천한 경우: 추천 취소(반대도 가능)\n\n"
                            + "_by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "추천 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/{reviewId}/recommendations")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<RecommendationDTO> toggleRecommendation(
            @AuthUser User user,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody @Valid ToggleRecommendationDTO request) {
        RecreationReviewRecommendation recommendation =
                recreationReviewService.toggleRecommendation(user, reviewId, request);

        return BaseResponse.of(
                SuccessStatus._CREATED,
                RecreationReviewConverter.toRecommendationDTO(recommendation));
    }
}
