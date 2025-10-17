package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.order.application.service.ReviewServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreateReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewStatusResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewWithReplyResponseDtoV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class ReviewControllerV1 {

    private final ReviewServiceV1 reviewServiceV1;

    public ReviewControllerV1(ReviewServiceV1 reviewServiceV1) {
        this.reviewServiceV1 = reviewServiceV1;
    }
    @Operation(summary = "리뷰 작성")
    @PostMapping("/{orderId}/review")
    public ResponseEntity<ApiResult<ReviewStatusResponseV1>> refundOrder(
            @PathVariable UUID orderId,
            @RequestBody CreateReviewRequestV1 req
    ) {
        return ResponseUtil.success("리뷰 작성 성공", reviewServiceV1.writeReview(orderId, req));
    }


    //내가 쓴 리뷰 조회
    @Operation(summary = "내가 쓴 리뷰 + 사장님 답글 목록 조회", description = "사용자가 작성한 Review와 이에 대한 OwnerReview(답글)를 함께 반환합니다. 페이지네이션: 페이지당 3건 고정.", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Page<ReviewWithReplyResponseDtoV1>>> getMyReviewsWithOwnerReplies(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page
    ) {
        if (userDetailsInfo == null) {
            return ResponseUtil.failure(ErrorCode.UNAUTHORIZED, "로그인되어 있지 않습니다.");
        }
        // 페이지 사이즈는 정책에 따라 3으로 고정
        int pageIndex = (page == null || page < 0) ? 0 : page;
        Pageable pageable = PageRequest.of(
                pageIndex,
                3,
                Sort.Direction.DESC, "createdDate"
        );


        Page<ReviewWithReplyResponseDtoV1> result =
                reviewServiceV1.getMyReviewsWithOwnerReplies(userDetailsInfo, pageable);

        return ResponseUtil.success("리뷰 및 답글 목록 조회 성공", result);
    }

    @Operation(summary = "내가 쓴 특정 리뷰 삭제", description = "로그인한 사용자가 자신이 작성한 리뷰를 삭제합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResult<Void>> deleteMyReview(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable("reviewId") UUID reviewId
    ) {
        if (userDetailsInfo == null) {
            return ResponseUtil.failure(ErrorCode.UNAUTHORIZED, "로그인되어 있지 않습니다.");
        }
        reviewServiceV1.deleteMyReview(userDetailsInfo, reviewId);
        return ResponseUtil.success("리뷰 삭제 성공", null);
    }

}
