package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.entity.Cart;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class InsertCartItemResponseV1 {
    private UUID cartId;
    private Integer userNo;
    private UUID storeId;
    private UUID productId;
    private Integer quantity;
    //private String message =  "장바구니에 상품이 성공적으로 추가되었습니다.";

    public InsertCartItemResponseV1(Cart cart) {
        this.cartId = cart.getCartId();
        this.userNo = cart.getUser().getUserNo();
        this.storeId = cart.getStore().getStoreId();
        this.productId = cart.getProduct().getProductId();
        this.quantity = cart.getQuantity();
    }
}
