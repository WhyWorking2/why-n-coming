package org.sparta.whyncoming.store.presentation.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateStoreResponseV1 {

    private final UUID storeId;
    private final String storeName;
    private final String storeLogoUrl;
    private final String storeImageUrl;
    private final Integer minDeliveryPrice;
    private final Integer deliveryTip;

    public CreateStoreResponseV1(UUID storeId, String storeName, String storeLogoUrl, String storeImageUrl, Integer minDeliveryPrice, Integer deliveryTip) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
        this.storeImageUrl = storeImageUrl;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
    }
}
