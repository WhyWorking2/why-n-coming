package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ConfirmPaymentResponseV1 {
    private String checkoutUrl;
}