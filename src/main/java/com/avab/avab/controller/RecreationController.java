package com.avab.avab.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.apiPayload.code.status.SuccessStatus;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewPageDTO;
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
    @Parameter(name = "user", hidden = true)
    @GetMapping("/popular")
    public BaseResponse<RecreationPreviewPageDTO> getTop9RecreationsByWeeklyViewCount(
            @AuthUser User user) {
        Page<Recreation> topRecreations = recreationService.getTop9RecreationsByWeeklyViewCount();

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewPageDTO(topRecreations, user));
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
    public BaseResponse<RecreationPreviewPageDTO> searchRecreations(
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
                RecreationConverter.toRecreationPreviewPageDTO(recreationPage, user));
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

    @Operation(summary = "레크레이션 리뷰 작성 API", description = "레크레이션에 리뷰를 작성합니다.")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "리뷰 생성 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/{recreationId}/reviews")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<RecreationReviewCreatedDTO> postRecreationReviewDTO(
            @AuthUser User user,
            @PathVariable("recreationId") @ExistRecreation Long recreationId,
            @Valid @RequestBody PostRecreationReviewDTO request) {
        RecreationReview review = recreationService.createReview(user, recreationId, request);

        return BaseResponse.of(
                SuccessStatus._CREATED, RecreationConverter.toRecreationReviewCreatedDTO(review));
    }

    @Operation(summary = "레크레이션 리뷰 목록 조회 API", description = "레크레이션 리뷰를 조회합니다.")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "리뷰 조회 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/{recreationId}/reviews")
    public BaseResponse<RecreationReviewPageDTO> getRecreationReviews(
            @AuthUser User user,
            @PathVariable("recreationId") @ExistRecreation Long recreationId,
            @RequestParam(name = "page", defaultValue = "0", required = false) @ValidatePage
                    Integer page) {

        Page<RecreationReview> reviewPage =
                recreationService.getRecreationReviews(recreationId, page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationReviewPageDTO(reviewPage, user));
    }

    @Operation(summary = "연관 레크레이션 API", description = "연관 레크레이션 목록을 가져옵니다. _by 수기_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/{recreationId}/related")
    public BaseResponse<List<RecreationPreviewDTO>> relatedRecreation(
            @AuthUser User user,
            @ExistRecreation @PathVariable(name = "recreationId") Long recreationId) {
        List<Recreation> relatedRecreation =
                recreationService.findRelatedRecreations(user, recreationId);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewListDTO(relatedRecreation, user));
    }
}
