package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossConfirmResponseV1 {
    private String status;
    private String paymentKey;
    private String orderId;
    private Integer totalAmount;

    public TossConfirmResponseV1(String status, String paymentKey, String orderId, Integer totalAmount) {
        this.status = status;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public static TossConfirmResponseV1 of(String status,  String paymentKey, String orderId, Integer totalAmount) {
        return new TossConfirmResponseV1(status, paymentKey, orderId, totalAmount);
    }
}