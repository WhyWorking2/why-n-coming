package org.sparta.whyncoming.order.presentation.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UpdateCartItemRequestV1 {

    private Integer userId;
    private UUID storeId;
    private UUID productId;

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;
}
