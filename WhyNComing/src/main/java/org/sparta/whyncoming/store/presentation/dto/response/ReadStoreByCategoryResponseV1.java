package org.sparta.whyncoming.store.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ReadStoreByCategoryResponseV1 {

    String storeLogoUrl;
    String storeImageUrl;     // 대표 이미지 1장
    String storeName;
    Integer storeReviewCount; // nullable 허용
    BigDecimal storeRating;   // numeric(2,1) → BigDecimal 권장
    Integer minDeliveryPrice;
}
