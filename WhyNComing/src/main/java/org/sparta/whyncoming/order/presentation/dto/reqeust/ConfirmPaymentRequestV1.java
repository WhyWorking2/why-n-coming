package org.sparta.whyncoming.order.presentation.dto.reqeust;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ConfirmPaymentRequestV1 {
    private String paymentKey; // Toss PG가 넘겨주는 키
    private String orderId;    // 우리 시스템의 주문번호
    private int amount;        // 결제 금액
}