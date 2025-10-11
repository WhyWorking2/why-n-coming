package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "배달 조회 응답 DTO")
public class DeliveryStatusResponseV1 {

    @Schema(description = "현재 위치 (위도, 경도)", example = "37.1234, 127.5678")
    private String position;

    @Schema(description = "예상 도착 시각", example = "2025-09-29T19:30:00")
    private String estimatedArrival;

    @Schema(description = "배달 상태", example = "DELIVERING")
    private String deliveryStatus;
}