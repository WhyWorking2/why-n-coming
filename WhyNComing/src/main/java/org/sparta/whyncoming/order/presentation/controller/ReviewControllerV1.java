package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.ReviewServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreateReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewStatusResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class ReviewControllerV1 {
    private final ReviewServiceV1 service;

    public ReviewControllerV1(ReviewServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "리뷰 작성")
    @PostMapping("/{orderId}/review")
    public ResponseEntity<ApiResult<ReviewStatusResponseV1>> refundOrder(
            @PathVariable UUID orderId,
            @RequestBody CreateReviewRequestV1 req
    ) {
        return ResponseUtil.success("리뷰 작성 성공", service.writeReview(orderId, req));
    }
}
