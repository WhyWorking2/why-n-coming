package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.DeliveryServiceV1;
import org.sparta.whyncoming.order.presentation.dto.response.DeliveryStatusResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class DeliveryControllerV1 {
    private final DeliveryServiceV1 service;

    public DeliveryControllerV1(DeliveryServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "배달 조회")
    @GetMapping("/{orderId}/delivery")
    public ResponseEntity<ApiResult<DeliveryStatusResponseV1>> readDeliveryStatus(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("배달 조회 성공", service.readDeliveryStatus(orderId));
    }

    @Operation(summary = "배달 수락")
    @PostMapping("/store/{orderId}/accept")
    public ResponseEntity<ApiResult<DeliveryStatusResponseV1>> acceptDelivery(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("배달 수락 성공", service.acceptDelivery(orderId));
    }

    @Operation(summary = "조리 완료")
    @PostMapping("/store/{orderId}/cooked")
    public ResponseEntity<ApiResult<DeliveryStatusResponseV1>> cookedDelivery(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("조리 완료 성공", service.cookedDelivery(orderId));
    }

    @Operation(summary = "배달 시작")
    @PostMapping("/store/{orderId}/deliverying")
    public ResponseEntity<ApiResult<DeliveryStatusResponseV1>> startDelivery(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("배달 시작 성공", service.startDelivery(orderId));
    }

    @Operation(summary = "배달 완료")
    @PostMapping("/store/{orderId}/deliveryed")
    public ResponseEntity<ApiResult<DeliveryStatusResponseV1>> completeDelivery(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("배달 완료 성공", service.completeDelivery(orderId));
    }
}
