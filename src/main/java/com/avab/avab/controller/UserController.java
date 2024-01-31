package com.avab.avab.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.converter.UserConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.UserRequestDTO.UpdateUserNameDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewPageDTO;
import com.avab.avab.dto.response.UserResponseDTO.UserResponse;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.UserService;
import com.avab.avab.validation.annotation.ValidatePage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User 👥", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "즐겨찾는 레크레이션 목록 조회 API",
            description = "즐겨찾기가 되어있는 레크레이션 목록을 조회합니다. 한 페이지에 6개까지 출력되고, 페이지는 인자로 받습니다. _by 루아_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @GetMapping("/me/favorites/recreations")
    @Parameter(name = "user", hidden = true)
    public BaseResponse<RecreationPreviewPageDTO> getFavoriteRecreations(
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page,
            @AuthUser User user) {
        Page<Recreation> recreationPage = userService.getFavoriteRecreations(user, page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewPageDTO(recreationPage, user));
    }

    @Operation(summary = "회원 정보 수정 API", description = "회원 닉네임을 수정합니다. 닉네임을 인자로 받습니다. _by 루아_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @PatchMapping("/me/name")
    public BaseResponse<UserResponse> updateUserName(
            @RequestBody @Valid UpdateUserNameDTO updateUserNameDTO, @AuthUser User user) {
        User updatedUser = userService.updateUserName(updateUserNameDTO.getName(), user);
        return BaseResponse.onSuccess(UserConverter.toUserResponse(updatedUser));
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

        Page<Flow> scrapflowPage = userService.getScrapFlows(user, page);

        return BaseResponse.onSuccess(
                FlowConverter.toFlowPreviewPageDTO(scrapflowPage, user));
    }

    @Operation(summary = "내 플로우 조회", description = "내가 만든 플로우를 최신순으로 조회합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/me/flows")
    public BaseResponse<FlowPreviewPageDTO> getMyFlows(
            @AuthUser User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page) {
        Page<Flow> flowPage = userService.getMyFlows(user, page);

        return BaseResponse.onSuccess(FlowConverter.toFlowPreviewPageDTO(flowPage, user));
    }
}
