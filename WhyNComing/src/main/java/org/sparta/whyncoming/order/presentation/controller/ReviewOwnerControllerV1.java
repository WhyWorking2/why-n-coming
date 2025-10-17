package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.order.application.service.ReviewOwnerServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreateOwnerReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.UpdateOwnerReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.CreateOwnerReviewResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.UpdateOwnerReviewResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/stores/{storeId}")
@Tag(name = "OwnerReview", description = "사장 리뷰 답변 API")
public class ReviewOwnerControllerV1 {

    private final ReviewOwnerServiceV1 service;

    public ReviewOwnerControllerV1(ReviewOwnerServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "가게 주인의 리뷰 답변 작성")
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/reviews/{reviewId}/ownerReviews")
    public ResponseEntity<ApiResult<CreateOwnerReviewResponseV1>> createOwnerReview(
            @AuthenticationPrincipal CustomUserDetailsInfo principal,
            @PathVariable UUID storeId,
            @PathVariable UUID reviewId,
            @Valid @RequestBody CreateOwnerReviewRequestV1 request
    ) {
        var result = service.createOwnerReview(principal, storeId, reviewId, request);
        return ResponseUtil.success("사장 답변 등록 완료", result);
    }

    @Operation(summary = "가게 주인의 리뷰 답변 수정")
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/reviews/{reviewId}/ownerReviews/{ownerReviewId}")
    public ResponseEntity<ApiResult<UpdateOwnerReviewResponseV1>> updateOwnerReview(
            @AuthenticationPrincipal CustomUserDetailsInfo principal,
            @PathVariable UUID storeId,
            @PathVariable UUID reviewId,
            @PathVariable UUID ownerReviewId,
            @Valid @RequestBody UpdateOwnerReviewRequestV1 request
    ) {
        var result = service.updateOwnerReviewById(principal, storeId, reviewId, ownerReviewId, request);
        return ResponseUtil.success("사장 답변 수정 완료", result);
    }
}
