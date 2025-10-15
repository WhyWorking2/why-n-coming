package org.sparta.whyncoming.order.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.whyncoming.order.application.service.PaymentServiceV1;
import org.sparta.whyncoming.order.presentation.dto.reqeust.ConfirmPaymentRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ConfirmPaymentResponseV1;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/order/payments")
@RequiredArgsConstructor
public class PaymentControllerV1 {

    private final PaymentServiceV1 service;

    @PostMapping("/ready")
    public ConfirmPaymentResponseV1 ready() throws Exception {
        return service.requestPayment();
    }

    @GetMapping("/success")
    public String paymentSuccess(ConfirmPaymentRequestV1 request) throws Exception {
        return service.confirmPayment(request);
    }

    @GetMapping("/fail")
    public String paymentFail() {
        return "결제 실패 또는 취소됨";
    }
}