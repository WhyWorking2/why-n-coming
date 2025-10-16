package org.sparta.whyncoming.order.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TossConfirmRequestV1 {
    private String paymentKey;
    private String orderId;
    private Integer amount;

    public TossConfirmRequestV1(String paymentKey, String orderId, Integer amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
