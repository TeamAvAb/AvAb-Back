package com.avab.avab.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.avab.avab.apiPayload.BaseResponse;
import com.avab.avab.apiPayload.code.status.SuccessStatus;
import com.avab.avab.controller.enums.SortCondition;
import com.avab.avab.converter.FlowConverter;
import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationReview;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.CreateRecreationDTO;
import com.avab.avab.dto.reqeust.RecreationRequestDTO.PostRecreationReviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.FavoriteDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewPageDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationRecommendDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewCreatedDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewPageDTO;
import com.avab.avab.security.handler.annotation.AuthUser;
import com.avab.avab.service.RecreationService;
import com.avab.avab.validation.annotation.ExistRecreation;
import com.avab.avab.validation.annotation.ValidatePage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
    @Parameter(name = "user", hidden = true)
    @GetMapping("/{recreationId}")
    public BaseResponse<DescriptionDTO> getRecreationDescription(
            @AuthUser User user,
            @ExistRecreation @PathVariable(name = "recreationId") Long recreationId) {
        Recreation recreation = recreationService.getRecreationDescription(recreationId);
        return BaseResponse.onSuccess(RecreationConverter.toDescriptionDTO(recreation, user));
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
            @RequestParam(name = "purpose", required = false) List<Purpose> purposes,
            @RequestParam(name = "gender", required = false) List<Gender> genders,
            @RequestParam(name = "age", required = false) List<Age> ages,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page,
            @RequestParam(name = "sortBy", required = false, defaultValue = "recent")
                    SortCondition sortCondition) {
        Page<Recreation> recreationPage =
                recreationService.searchRecreations(
                        user,
                        searchKeyword,
                        keywords,
                        participants,
                        playTime,
                        places,
                        purposes,
                        genders,
                        ages,
                        page,
                        sortCondition);

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

    @Operation(summary = "레크레이션 리뷰 작성 API", description = "레크레이션에 리뷰를 작성합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "리뷰 생성 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping("/{recreationId}/reviews")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse<RecreationReviewCreatedDTO> postRecreationReview(
            @AuthUser User user,
            @PathVariable("recreationId") @ExistRecreation Long recreationId,
            @Valid @RequestBody PostRecreationReviewDTO request) {
        RecreationReview review = recreationService.createReview(user, recreationId, request);

        return BaseResponse.of(
                SuccessStatus._CREATED, RecreationConverter.toRecreationReviewCreatedDTO(review));
    }

    @Operation(summary = "레크레이션 리뷰 목록 조회 API", description = "레크레이션 리뷰를 조회합니다. _by 보노_")
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
    @GetMapping("/{recreationId}/related/recreations")
    public BaseResponse<List<RecreationPreviewDTO>> relatedRecreation(
            @AuthUser User user,
            @ExistRecreation @PathVariable(name = "recreationId") Long recreationId) {
        List<Recreation> relatedRecreation =
                recreationService.findRelatedRecreations(user, recreationId);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewListDTO(relatedRecreation, user));
    }

    @Operation(summary = "레크레이션 게시 API", description = "레크레이션을 게시합니다. _by 보노_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON201", description = "게시 성공")})
    @Parameter(name = "user", hidden = true)
    @PostMapping(
            value = "",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<RecreationCreatedDTO> createRecreation(
            @AuthUser User user,
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                    @RequestPart("request")
                    CreateRecreationDTO request,
            @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnailImage,
            @Parameter(
                            description =
                                    "순서 이미지, 반드시 파일 명 앞에 순서(0부터 시작) 명시할 것! **{순서}_{파일명}** _ex)0_image.jpg_")
                    @RequestPart(name = "wayImages", required = false)
                    List<MultipartFile> wayImages) {

        Recreation recreation =
                recreationService.createRecreation(user, request, thumbnailImage, wayImages);
        return BaseResponse.of(
                SuccessStatus._CREATED, RecreationConverter.toRecreationCreatedDTO(recreation));
    }

    @Operation(summary = "연관 플로우 API", description = "연관 플로우 목록을 가져옵니다. _by 수기_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("/{recreationId}/related/flows")
    public BaseResponse<List<FlowDetailPageDTO>> relatedFlow(
            @AuthUser User user,
            @ExistRecreation @PathVariable(name = "recreationId") Long recreationId) {
        List<Flow> relatedFlow = recreationService.findRelatedFlows(recreationId);

        return BaseResponse.onSuccess(FlowConverter.toFlowDetailListDTO(relatedFlow, user));
    }

    @Operation(
            summary = "레크레이션 추천 API",
            description = "키워드, 목적 등 여러 request 정보를 통해 추천 레크레이션을 만듭니다. _by 제이미_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @GetMapping("/recommended")
    public BaseResponse<List<RecreationRecommendDTO>> recommendRecreations(
            @Parameter(name = "user", hidden = true) @AuthUser User user,
            @RequestParam(name = "keyword", required = false) List<Keyword> keywords,
            @RequestParam(name = "participants", required = false) Integer participants,
            @RequestParam(name = "playTime") Integer playTime,
            @RequestParam(name = "purpose") List<Purpose> purposes,
            @RequestParam(name = "gender", required = false) List<Gender> genders,
            @RequestParam(name = "age", required = false) List<Age> ages) {

        List<Recreation> recommendRecreations =
                recreationService.recommendRecreations(
                        keywords, participants, playTime, purposes, genders, ages);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationRecommendListDTO(recommendRecreations, user));
    }

    @Operation(summary = "최신 레크레이션 API", description = "최신 레크레이션 목록을 가져옵니다. _by 수기_")
    @ApiResponses({@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
    @Parameter(name = "user", hidden = true)
    @GetMapping("")
    public BaseResponse<RecreationPreviewPageDTO> getRecentRecreation(
            @AuthUser User user,
            @RequestParam(name = "page", required = false, defaultValue = "0") @ValidatePage
                    Integer page) {
        Page<Recreation> recentRecreations = recreationService.getRecentRecreation(page);

        return BaseResponse.onSuccess(
                RecreationConverter.toRecreationPreviewPageDTO(recentRecreations, user));
    }
}
