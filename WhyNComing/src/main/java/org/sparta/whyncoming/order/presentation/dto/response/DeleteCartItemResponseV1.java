package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteCartItemResponseV1 {

    private UUID cartId;
    private String message = "장바구니 상품이 삭제됐습니다.";

    public DeleteCartItemResponseV1(UUID cartId){
        this.cartId = cartId;
    }
}
