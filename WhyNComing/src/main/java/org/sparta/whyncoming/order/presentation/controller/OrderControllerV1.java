package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.OrderServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreateOrderRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.CreatePaymentRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class OrderControllerV1 {
    private final OrderServiceV1 service;

    public OrderControllerV1(OrderServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "주문 요청")
    @PostMapping
    public ResponseEntity<ApiResult<OrderStatusResponseV1>> createOrder(
            @RequestBody CreateOrderRequestV1 req
    ) {
        return ResponseUtil.success("주문 생성 성공", service.createOrder(req));
    }

    @Operation(summary = "결제(API만)")
    @PostMapping("/{orderId}/payment-api")
    public ResponseEntity<ApiResult<OrderStatusResponseV1>> createPayment(
            @PathVariable UUID orderId,
            @RequestBody CreatePaymentRequestV1 req
    ) {
        return ResponseUtil.success("주문 생성 성공", service.createPayment(orderId, req));
    }

    @Operation(summary = "주문 취소")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResult<OrderStatusResponseV1>> cancelOrder(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("주문 취소 성공", service.cancelOrder(orderId));
    }

    @Operation(summary = "환불")
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<ApiResult<OrderStatusResponseV1>> refundOrder(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("주문 환불 성공", service.refundOrder(orderId));
    }

    @Operation(summary = "주문 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<GetOrderListResponseV1>>> readOrderList(
            @RequestParam(required = false) Integer UserNo
    ) {
        return ResponseUtil.success("주문 리스트 조회 성공", service.getOrderList(UserNo));
    }

    @Operation(summary = "주문 상세 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResult<GetOrderDetailResponseV1>> readOrderDetail(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("주문 리스트 조회 성공", service.getOrderDetail(orderId));
    }

    @Operation(summary = "입점주 주문 리스트 조회")
    @GetMapping("/store")
    public ResponseEntity<ApiResult<List<GetStoreOrderListResponseV1>>> getStoreOrderList(
            @RequestParam(required = false) UUID storeId
    ) {
        return ResponseUtil.success("주문 리스트 조회 성공", service.getStoreOrderList(storeId));
    }

    @Operation(summary = "입점주 주문 상세 조회")
    @GetMapping("/store/{orderId}")
    public ResponseEntity<ApiResult<GetStoreOrderDetailResponseV1>> getStoreOrderDetail(
            @PathVariable UUID orderId
    ) {
        return ResponseUtil.success("주문 리스트 조회 성공", service.getStoreOrderDetail(orderId));
    }
}
