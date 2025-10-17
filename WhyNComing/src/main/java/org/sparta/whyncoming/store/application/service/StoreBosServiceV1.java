package org.sparta.whyncoming.store.application.service;

import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.entity.StoreImage;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.store.presentation.dto.response.ReadStoreBosResponseV1;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoreBosServiceV1 {

    private final StoreRepository storeRepository;

    public StoreBosServiceV1(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // 관리자 입점사 전체 조회 (삭제 포함)
    public List<ReadStoreBosResponseV1> getStores() {

        List<Store> stores = storeRepository.findAllWithImagesForAdmin();

        return stores.stream()
                .map(this::toReadBosResponse)
                .collect(Collectors.toList());
    }

    // 관리자 특정 입점사 조회
    public ReadStoreBosResponseV1 getStore(UUID storeId) {
        Store store = storeRepository.findByIdWithImages(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 입점사를 찾을 수 없습니다. storeId=" + storeId));

        return toReadBosResponse(store);
    }

    private ReadStoreBosResponseV1 toReadBosResponse(Store store) {
        List<String> storeImageUrls = store.getStoreImages() == null ? List.of() :
                store.getStoreImages().stream()
                        .map(StoreImage::getStoreImageUrl)
                        .collect(Collectors.toList());

        return ReadStoreBosResponseV1.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeLogoUrl(store.getStoreLogoUrl())
                .storeImageUrls(storeImageUrls)
                .minDeliveryPrice(store.getMinDeliveryPrice())
                .deliveryTip(store.getDeliveryTip())
                .operationHours(store.getOperationHours())
                .deliveryAddress(store.getDeliveryAddress())

                .createdBy(store.getCreatedBy())
                .modifiedBy(store.getModifiedBy())
                .createdDate(store.getCreatedDate())
                .modifiedDate(store.getModifiedDate())
                .deletedDate(store.getDeletedDate())
                .build();
    }

}
