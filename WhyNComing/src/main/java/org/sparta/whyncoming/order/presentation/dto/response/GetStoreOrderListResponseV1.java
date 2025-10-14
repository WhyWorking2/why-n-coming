package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Schema(description = "입점주 주문 리스트 조회 응답 DTO")
public class GetStoreOrderListResponseV1 {

    @Schema(description = "주문 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID orderId;

    @Schema(description = "주문 상태", example = "DELIVERED")
    private String status;

    @Schema(description = "유저 ID", example = "101")
    private Integer userId;
}