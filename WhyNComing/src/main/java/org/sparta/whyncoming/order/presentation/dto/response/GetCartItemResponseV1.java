package org.sparta.whyncoming.order.presentation.dto.response;

import org.sparta.whyncoming.order.domain.entity.Cart;

import java.util.UUID;

public class GetCartItemResponseV1 {

    private UUID cartId;
    private Integer userId;
    private UUID storeId;
    private UUID productId;
    private int quantity;

    public GetCartItemResponseV1(Cart cart){
        this.cartId = cart.getCartId();
        this.userId = cart.getUser().getUserNo();
        this.storeId = cart.getStore().getStoreId();
        this.productId = cart.getProduct().getProductId();

    }
}
