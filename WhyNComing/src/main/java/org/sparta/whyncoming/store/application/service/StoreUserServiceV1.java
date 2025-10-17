package org.sparta.whyncoming.store.application.service;

import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.entity.StoreImage;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.store.presentation.dto.response.ReadStoreByCategoryResponseV1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class StoreUserServiceV1 {

    private final StoreRepository storeRepository;

    public StoreUserServiceV1(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<ReadStoreByCategoryResponseV1> getStoresByCategory(UUID categoryId) {
        List<Store> stores = storeRepository.findActiveStoresWithImagesByCategory(categoryId);

        // 필요 시 정렬: 평점 desc → 리뷰수 desc
        stores.sort(
                Comparator.comparing(Store::getStoreRating, Comparator.nullsLast(Comparator.naturalOrder())).reversed()
                        .thenComparing(Store::getStoreReviewCount, Comparator.nullsLast(Comparator.naturalOrder())).reversed()
        );

        return stores.stream()
                .map(this::toReadByCategory)
                .toList();
    }

    private ReadStoreByCategoryResponseV1 toReadByCategory(Store s) {
        String firstImageUrl = (s.getStoreImages() == null || s.getStoreImages().isEmpty())
                ? null
                : s.getStoreImages().stream()
                // 대표 선정 룰: 생성일/정렬 기준이 있다면 여기서 Comparator로 선정
                .sorted(Comparator.comparing(StoreImage::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(StoreImage::getStoreImageUrl)
                .findFirst()
                .orElse(null);

        return ReadStoreByCategoryResponseV1.builder()
                .storeLogoUrl(s.getStoreLogoUrl())
                .storeImageUrl(firstImageUrl)
                .storeName(s.getStoreName())
                .storeReviewCount(s.getStoreReviewCount())
                .storeRating(s.getStoreRating())          // BigDecimal
                .minDeliveryPrice(s.getMinDeliveryPrice())
                .build();
    }
}
