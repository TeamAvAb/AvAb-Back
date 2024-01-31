package com.avab.avab.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.FlowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flows")
@RequiredArgsConstructor
@Validated
@Tag(name = "Flow", description = "플로우 관련 API")
public class FlowController {

    private final FlowService flowService;

    @Operation(summary = "플로우 상세설명 조회 API", description = "플로우 상세설명을 조회합니다. _by 루아_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @GetMapping("{flowId}")
    public BaseResponse<FlowDetailDTO> getFlowDetail(
            @AuthUser User user, @PathVariable Long flowId) {

        Flow flow = flowService.getFlowDetail(flowId);
        return BaseResponse.onSuccess(FlowConverter.toFlowDetailDTO(flow, user));
    }
}
