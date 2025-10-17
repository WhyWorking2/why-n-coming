package org.sparta.whyncoming.store.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ReadStoreBosResponseV1 {

    private final UUID storeId;
    private final String storeName;
    private final String storeLogoUrl;
    private final List<String> storeImageUrls;
    private final Integer minDeliveryPrice;
    private final Integer deliveryTip;
    private final String operationHours;
    private final String deliveryAddress;

    private final Integer createdBy;
    private final Integer modifiedBy;
    private final Instant createdDate;
    private final Instant modifiedDate;
    private final Instant deletedDate;

    public ReadStoreBosResponseV1(UUID storeId, String storeName, String storeLogoUrl, List<String> storeImageUrls, Integer minDeliveryPrice, Integer deliveryTip, String operationHours, String deliveryAddress, Integer createdBy, Integer modifiedBy, Instant createdDate, Instant modifiedDate, Instant deletedDate) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
        this.storeImageUrls = storeImageUrls;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.deliveryAddress = deliveryAddress;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
    }
}
