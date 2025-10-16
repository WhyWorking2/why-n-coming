package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.TossPaymentServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.TossConfirmRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.TossConfirmResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "주문 데이터 API")
public class TossPaymentControllerV1 {

    private final TossPaymentServiceV1 service;

    public TossPaymentControllerV1(TossPaymentServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "결제(Toss Payments 사용)")
    @GetMapping("/{orderId}/payment")
    public RedirectView showPaymentPage(@PathVariable String orderId) {
        return new RedirectView("/payment.html?orderId=" + orderId);
    }

    /*
    @PostMapping("/{orderId}/payment/confirm")
    public ResponseEntity<TossConfirmResponseV1> confirm(@RequestBody TossConfirmRequestV1 req) {
        TossConfirmResponseV1 res = service.confirmPayment(req);
        return ResponseEntity.ok(res);
    }*/

    @Operation(summary = "결제 성공")
    @GetMapping("/{orderId}/payment/success")
    public ResponseEntity<ApiResult<TossConfirmResponseV1>> paymentSuccess(
            @PathVariable UUID orderId,
            @RequestParam String paymentKey,
            @RequestParam Integer amount
    ) {

        TossConfirmRequestV1 req = new TossConfirmRequestV1(paymentKey, orderId.toString(), amount);
        log.info("✅ Toss Confirm Request: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);
        TossConfirmResponseV1 res = service.confirmPayment(req);

        return ResponseUtil.success("주문 생성 성공", res);
    }

    @Operation(summary = "결제 실패")
    @GetMapping("/{orderId}/payment/fail")
    public ResponseEntity<?> paymentFail(
            @PathVariable UUID orderId,
            @RequestParam Map<String, String> failData
    ) {
        failData.put("orderId", orderId.toString());
        return ResponseUtil.failure(
                ErrorCode.INTERNAL_SERVER_ERROR, "Toss 결제 실패: " + failData.get("message")
        );
        //return ResponseEntity.badRequest().body(failData);
    }
}