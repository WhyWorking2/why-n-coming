package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "주문 상태 응답 DTO")
public class OrderStatusResponseV1 {

    @Schema(description = "주문 상태", example = "SUCCESS")
    private String orderStatus;
}