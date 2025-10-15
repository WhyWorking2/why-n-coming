package org.sparta.whyncoming.order.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class AddCartItemRequestV1 {

    private Long storeId; // 일단 Long으로 해둠, 나중에 UUID로 수정
    private Long productId; // ''
    private int quantity;
}
