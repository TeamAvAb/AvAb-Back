package com.avab.avab.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.dto.recreation.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.recreation.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.service.RecreationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recreations")
@RequiredArgsConstructor
public class RecreationController {

    private final RecreationService recreationService;

    @Operation(summary = "인기 레크레이션 목록 조회 API", description = "조회수를 기준으로 인기 레크레이션 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/popular")
    public BaseResponse<List<PopularRecreationListDTO>> getTop3RecreationsByViewCount() {
        return BaseResponse.onSuccess(recreationService.getTop3RecreationsByViewCount());
    }

    @Operation(summary = "레크레이션 상세설명 조회 API", description = "레크레이션 상세설명을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/{recreationId}/description")
    public BaseResponse<DescriptionDTO> getRecreationDescription(
            @PathVariable(name = "recreationId") Long recreationId) {
        return BaseResponse.onSuccess(recreationService.getRecreationDescription(recreationId));
    }
}
