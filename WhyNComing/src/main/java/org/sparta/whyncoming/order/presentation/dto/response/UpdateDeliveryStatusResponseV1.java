package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "배달 상태 변경 응답 DTO")
public class UpdateDeliveryStatusResponseV1 {

    @Schema(description = "배달 상태", example = "ACCEPTED")
    private String deliveryStatus;
}