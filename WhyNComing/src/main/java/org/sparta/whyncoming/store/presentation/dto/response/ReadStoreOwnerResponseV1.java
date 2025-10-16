package org.sparta.whyncoming.store.presentation.dto.response;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ReadStoreOwnerResponseV1 {

    private final UUID storeId;
    private final String storeName;
    private final String storeLogoUrl;
    private final List<String> storeImageUrls;
    private final Integer minDeliveryPrice;
    private final Integer deliveryTip;
    private final String operationHours;
    private final String deliveryAddress;

    public ReadStoreOwnerResponseV1(UUID storeId, String storeName, String storeLogoUrl, List<String> storeImageUrls, Integer minDeliveryPrice, Integer deliveryTip, String operationHours, String deliveryAddress) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
        this.storeImageUrls = storeImageUrls;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.deliveryAddress = deliveryAddress;
    }
}
