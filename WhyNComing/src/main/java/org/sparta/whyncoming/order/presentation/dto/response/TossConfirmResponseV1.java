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
}