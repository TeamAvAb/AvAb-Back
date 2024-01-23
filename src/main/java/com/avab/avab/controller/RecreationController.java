package com.avab.avab.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewListDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.RecreationService;
import com.avab.avab.validation.annotation.ExistRecreation;
import com.avab.avab.validation.annotation.ValidatePage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recreations")
@RequiredArgsConstructor
@Validated
@Tag(name = "Recreation 🎲", description = "레크레이션 관련 API")
public class RecreationController {

    private final RecreationService recreationService;

    @Operation(
            summary = "인기 레크레이션 목록 조회 API",
            description = "조회수를 기준으로 인기 레크레이션 목록을 조회합니다. _by 루아_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/popular")
    public BaseResponse<List<PopularRecreationListDTO>> getTop3RecreationsByViewCount() {
        return BaseResponse.onSuccess(recreationService.getTop3RecreationsByViewCount());
    }

    @Operation(summary = "레크레이션 상세설명 조회 API", description = "레크레이션 상세설명을 조회합니다. _by 수기_")
    @ApiResponses({
        @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/{recreationId}")
    public BaseResponse<DescriptionDTO> getRecreationDescription(
            @ExistRecreation @PathVariable(name = "recreationId") Long recreationId) {
        Recreation recreation = recreationService.getRecreationDescription(recreationId);
        return BaseResponse.onSuccess(RecreationConverter.toDescriptionDTO(recreation));
    }

    @Operation(summary = "레크레이션 검색 API", description = "검색 키워드와 세부 필터를 이용해 레크레이션을 검색합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/search")
    public BaseResponse<RecreationPreviewListDTO> searchRecreations(
            @AuthUser User user,
            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(name = "keyword", required = false) List<Keyword> keywords,
            @RequestParam(name = "participants", required = false) Integer participants,
            @RequestParam(name = "playTime", required = false) Integer playTime,
            @RequestParam(name = "place", required = false) List<Place> places,
            @RequestParam(name = "gender", required = false) List<Gender> genders,
            @RequestParam(name = "age", required = false) List<Age> ages,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page) {
        Page<Recreation> recreationPage =
                recreationService.searchRecreations(
                        user,
                        searchKeyword,
                        keywords,
                        participants,
                        playTime,
                        places,
                        genders,
                        ages,
                        page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewListDTO(recreationPage, user));
    }

    @Operation(
            summary = "레크레이션 즐겨찾기 추가/취소 API",
            description = "레크레이션 즐겨찾기 안 했다면 즐겨찾기 추가, 했다면 즐겨찾기 취소합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/{recreationId}/favorites")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<FavoriteDTO> toggleFavoriteRecreation(
            @PathVariable(name = "recreationId") @ExistRecreation Long recreationId,
            @AuthUser User user) {
        Boolean isFavorite = recreationService.toggleFavoriteRecreation(recreationId, user);

        return BaseResponse.onSuccess(RecreationConverter.toFavoriteDTO(isFavorite));
    }

    @Operation(
            summary = "즐겨찾는 레크레이션 목록 조회 API",
            description = "즐겨찾기가 되어있는 레크레이션 목록을 조회합니다. 한 페이지에 6개까지 출력되고, 페이지는 인자로 받습니다. _by 루아_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @GetMapping("/favorites")
    @Parameter(name = "user", hidden = true)
    public BaseResponse<RecreationPreviewListDTO> getFavoriteRecreations(
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page,
            @AuthUser User user) {
        Page<Recreation> recreationPage = recreationService.getFavoriteRecreations(user, page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewListDTO(recreationPage, user));
    }
}
