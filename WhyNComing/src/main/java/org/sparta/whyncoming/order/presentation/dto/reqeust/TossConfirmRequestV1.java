package org.sparta.whyncoming.order.presentation.dto.reqeust;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class TossConfirmRequestV1 {
    private String paymentKey;
    private UUID orderId;
    private Integer totalPrice;
    private String contactNumber;
    private String requests;

    public TossConfirmRequestV1(String paymentKey, UUID orderId, String contactNumber, String requests) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.contactNumber = contactNumber;
        this.requests = requests;
    }

    public void updateTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}
