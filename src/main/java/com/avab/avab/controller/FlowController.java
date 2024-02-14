package com.avab.avab.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.dto.response.FlowResponseDTO.DeletedFlowDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailPageDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowScrapDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.FlowService;
import com.avab.avab.validation.annotation.ExistFlow;
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

    @Operation(summary = "플로우 상세설명 조회 API", description = "플로우 상세설명을 조회합니다. _by 루아_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @GetMapping("{flowId}")
    public BaseResponse<FlowDetailPageDTO> getFlowDetail(
            @AuthUser User user, @ExistFlow @PathVariable("flowId") Long flowId) {

        Flow flow = flowService.getFlowDetail(flowId);
        return BaseResponse.onSuccess(FlowConverter.toFlowDetailDTO(flow, user));
    }

    @Operation(summary = "플로우 목록 조회 API", description = "최신순으로 플로우를 조회합니다. _by 보노_")
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

    @Operation(
            summary = "플로우 생성 API",
            description =
                    """
                플로우를 생성합니다. \n
                seq는 플로우에 들어갈 레크레이션의 순서이고 필수입니다. \n
                원래 있는 레크레이션은 recreationId를 넣어주시면 되고, \n
                사용자가 직접 입력한 레크레이션은 customTitle, customKeywordList를 넣어주시면 됩니다. \n
                _by 루아_
                """)
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON201", description = "게시 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<FlowPreviewDTO> postFlow(
            @AuthUser User user, @RequestBody PostFlowDTO request) {
        Flow flow = flowService.postFlow(request, user);
        return BaseResponse.onSuccess(FlowConverter.toFlowPreviewDTO(flow, user));
    }

    @Operation(
            summary = "플로우 스크랩 API",
            description = "스크랩 되어 있지 않으면 스크랩, 되어 있지 않으면 스크랩 취소합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "스크랩 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/{flowId}/scraps")
    public BaseResponse<FlowScrapDTO> toggleScrapeFlow(
            @AuthUser User user, @PathVariable("flowId") @ExistFlow Long flowId) {
        Boolean isScraped = flowService.toggleScrapeFlow(user, flowId);
        return BaseResponse.onSuccess(FlowConverter.toFlowScrapDTO(isScraped));
    }

    @Operation(summary = "플로우 삭제 API", description = "플로우를 삭제합니다. _by 루아_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameter(name = "user", hidden = true)
    @DeleteMapping("/delete/{flowId}")
    public BaseResponse<DeletedFlowDTO> deleteFlow(
            @AuthUser User user, @PathVariable @ExistFlow Long flowId) {
        flowService.deleteFlow(flowId, user);
        return BaseResponse.onSuccess(FlowConverter.toDeletedFlowDTO(flowId));
    }

    @Operation(summary = "플로우 추천 API", description = "플로우 생성시 플로우를 추천합니다. _by 수기_")
    @ApiResponses(@ApiResponse(responseCode = "COMMON200", description = "OK, 성공"))
    @Parameter(name = "user", hidden = true)
    @GetMapping("/recommended")
    public BaseResponse<List<FlowDetailPageDTO>> recommendFlows(
            @AuthUser User user,
            @RequestParam(name = "keyword", required = false) List<Keyword> keywords,
            @RequestParam(name = "participants", required = false) Integer participants,
            @RequestParam(name = "playTime") Integer totalPlayTime,
            @RequestParam(name = "purpose") List<Purpose> purposes,
            @RequestParam(name = "gender", required = false) List<Gender> genders,
            @RequestParam(name = "age", required = false) List<Age> ages) {
        List<Flow> recommendFlows =
                flowService.recommendFlows(
                        keywords, participants, totalPlayTime, purposes, genders, ages);

        return BaseResponse.onSuccess(FlowConverter.toFlowDetailListDTO(recommendFlows, user));
    }
}
