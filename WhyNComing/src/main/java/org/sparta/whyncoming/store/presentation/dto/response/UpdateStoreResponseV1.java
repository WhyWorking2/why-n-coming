package org.sparta.whyncoming.store.presentation.dto.response;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateStoreResponseV1 {

    private final UUID storeId;
    private final String storeName;
    private final String storeLogoUrl;
    private final String storeImageUrl;
    private final Integer minDeliveryPrice;
    private final Integer deliveryTip;
    private final String operationHours;
    private final String deliveryAddress;

    public UpdateStoreResponseV1(UUID storeId, String storeName, String storeLogoUrl, String storeImageUrl, Integer minDeliveryPrice, Integer deliveryTip, String operationHours, String deliveryAddress) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
        this.storeImageUrl = storeImageUrl;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.deliveryAddress = deliveryAddress;
    }
}
