package org.sparta.whyncoming.order.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.order.application.service.TossPaymentServiceV1;
import org.sparta.whyncoming.order.presentation.dto.request.TossConfirmRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.PaymentInfoResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.TossConfirmResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Toss Order", description = "Toss 주문 데이터 API")
public class TossPaymentControllerV1 {

    private final TossPaymentServiceV1 service;

    public TossPaymentControllerV1(TossPaymentServiceV1 service) {
        this.service = service;
    }

    @Operation(summary = "Toss Client Key 반환(HTML 프런트엔드에서만 가능)")
    @GetMapping("/{orderId}/payment/client-key")
    public Map<String, Object> getClientKey(@PathVariable UUID orderId) {
        return service.getClientKey(orderId);
    }

    @Operation(summary = "Toss Payments 결제(HTML 프런트엔드에서만 가능)")
    @GetMapping("/{orderId}/payment")
    public RedirectView showPaymentPage(@PathVariable String orderId) {
        return new RedirectView("/payment.html?orderId=" + orderId);
    }

    @Operation(summary = "TOSS 결제 성공(HTML 프런트엔드에서만 가능)")
    @GetMapping("/{orderId}/payment/success")
    public ResponseEntity<ApiResult<TossConfirmResponseV1>> paymentSuccess(
            @PathVariable UUID orderId,
            @RequestParam String paymentKey,
            @RequestParam Integer amount
    ) {
        TossConfirmRequestV1 req = new TossConfirmRequestV1(paymentKey, orderId.toString(), amount);
        TossConfirmResponseV1 res = service.confirmPayment(req);

        return ResponseUtil.success("주문 생성 성공", res);
    }

    @Operation(summary = "TOSS 결제 실패(HTML 프런트엔드에서만 가능)")
    @GetMapping("/{orderId}/payment/fail")
    public ResponseEntity<?> paymentFail(
            @PathVariable UUID orderId,
            @RequestParam Map<String, String> failData
    ) {
        failData.put("orderId", orderId.toString());
        return ResponseUtil.failure(
                ErrorCode.INTERNAL_SERVER_ERROR, "Toss 결제 실패: " + failData.get("message")
        );
    }

    @Operation(summary = "TOSS 결제 조회")
    @GetMapping("/{orderId}/payment/{paymentKey}")
    public ResponseEntity<?> getPaymentInfo(
            @PathVariable String paymentKey,
            @PathVariable UUID orderId
    ) {
        PaymentInfoResponseV1 res = service.getPaymentInfo(paymentKey, orderId);
        return ResponseUtil.success("주문 생성 성공", res);
    }
}