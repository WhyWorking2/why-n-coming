package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Getter;
import org.sparta.whyncoming.order.domain.entity.Cart;

import java.util.UUID;

@Getter
public class AddCartItemResponseV1 {
    private UUID cartId;
    private Integer userId;
    private UUID storeId;
    private UUID productId;
    private int quantity;
    //private String message =  "장바구니에 상품이 성공적으로 추가되었습니다.";

    public AddCartItemResponseV1(UUID cartId, Integer userId, UUID storeId, UUID productId, int quantity){
        this.cartId = cartId;
        this.userId = userId;
        this.storeId = storeId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public AddCartItemResponseV1(Cart cart) {
        this.cartId = cart.getCartId();
        this.userId = cart.getUser().getUserNo();
        this.storeId = cart.getStore().getStoreId();
        this.productId = cart.getProduct().getProductId();
        this.quantity = cart.getQuantity();
    }
}
