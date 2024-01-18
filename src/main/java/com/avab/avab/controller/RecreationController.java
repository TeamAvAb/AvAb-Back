package com.avab.avab.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewListDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.RecreationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recreations")
@RequiredArgsConstructor
@Tag(name = "Recreation 🎲", description = "레크레이션 관련 API")
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

    @Operation(summary = "레크레이션 검색 API", description = "검색 키워드와 세부 필터를 이용해 레크레이션을 검색합니다.")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/search")
    public BaseResponse<RecreationPreviewListDTO> searchRecreations(
            @AuthUser User user,
            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(name = "keyword", required = false) Keyword keyword,
            @RequestParam(name = "participants", required = false) Integer participants,
            @RequestParam(name = "playTime", required = false) Integer playTime,
            @RequestParam(name = "place", required = false) Place place,
            @RequestParam(name = "gender", required = false) Gender gender,
            @RequestParam(name = "age", required = false) Age age,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        Page<Recreation> recreationPage =
                recreationService.searchRecreations(
                        user,
                        searchKeyword,
                        keyword,
                        participants,
                        playTime,
                        place,
                        gender,
                        age,
                        page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewListDTO(recreationPage, user));
    }
}
