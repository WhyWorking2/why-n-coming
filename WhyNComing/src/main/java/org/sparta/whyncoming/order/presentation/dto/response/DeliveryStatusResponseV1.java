package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;

@Getter
@NoArgsConstructor
@Schema(description = "배달 조회 응답 DTO")
public class DeliveryStatusResponseV1 {

    @Schema(description = "현재 위치 (위도, 경도)", example = "37.1234, 127.5678")
    private String position;

    @Schema(description = "배달 상태", example = "DELIVERING")
    private String deliveryStatus;

    public DeliveryStatusResponseV1(String position, DeliveryStatus deliveryStatus) {
        this.position = position;
        this.deliveryStatus = deliveryStatus.name();
    }

    public DeliveryStatusResponseV1(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus.name();
    }

    public static DeliveryStatusResponseV1 of(String position, DeliveryStatus deliveryStatus) {
        return new DeliveryStatusResponseV1(position, deliveryStatus);
    }
}