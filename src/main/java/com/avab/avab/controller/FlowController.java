package com.avab.avab.controller;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.FlowService;
import com.avab.avab.validation.annotation.ValidatePage;

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
@Tag(name = "Flow 🌊", description = "플로우 관련 API")
public class FlowController {

    private final FlowService flowService;

    //    private final UserRepository userRepository;

    @Operation(summary = "플로우 조회 API", description = "최신순으로 플로우를 조회합니다. _by 보노_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @GetMapping("")
    public BaseResponse<FlowPreviewPageDTO> getFlows(
            @AuthUser User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page) {
        Page<Flow> flowPage = flowService.getFlows(page);

        return BaseResponse.onSuccess(FlowConverter.toFlowPreviewPageDTO(flowPage, user));
    }

    @Operation(summary = "스크랩 플로우 API", description = "스크랩한 플로우 목록을 조회합니다. _by 수기_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @GetMapping("/scrap")
    public BaseResponse<FlowPreviewPageDTO> getScrapFlows(
            @AuthUser User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page) {

        Page<FlowFavorite> ScrapflowPage = flowService.getScrapFlows(user, page);

        return BaseResponse.onSuccess(
                FlowConverter.toFlowPreviewPageDTO(ScrapflowPage.map(FlowFavorite::getFlow), user));
    }
}
