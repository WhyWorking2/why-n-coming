package org.sparta.whyncoming.store.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.s3.S3Util;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.entity.StoreImage;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.store.presentation.dto.request.CreateStoreRequestV1;
import org.sparta.whyncoming.store.presentation.dto.response.CreateStoreResponseV1;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceV1 {

    private final StoreRepository storeRepository;
    private final S3Util s3Util;


    public StoreServiceV1(StoreRepository storeRepository, S3Util s3Util) {
        this.storeRepository = storeRepository;
        this.s3Util = s3Util;
    }

    // 가게 주인 입점사 전체 조회
    @Transactional
    public List<CreateStoreResponseV1> getAllActiveStores() {
        return storeRepository.findAllByDeletedDateIsNull()
                .stream()
                .filter(store -> !store.isDeleted())
                .map(store -> new CreateStoreResponseV1(
                        store.getStoreId(),
                        store.getStoreName(),
                        store.getStoreLogoUrl(),
                        store.getStoreImages().isEmpty() ? null : store.getStoreImages().get(0).getStoreImageUrl(),
                        store.getMinDeliveryPrice(),
                        store.getDeliveryTip()
                ))
                .toList();
    }

    @Transactional
    public CreateStoreResponseV1 getStoreById(Long storeId) {
        Store store = storeRepository.findByStoreIdAndDeletedDateIsNull(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        if (store.isDeleted()) {
            throw new IllegalStateException("삭제된 가게입니다.");
        }

        return new CreateStoreResponseV1(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreLogoUrl(),
                store.getStoreImages().isEmpty() ? null : store.getStoreImages().get(0).getImageUrl(),
                store.getMinDeliveryPrice(),
                store.getDeliveryTip()
        );
    }

    // 가게 주인 입점사 추가
    @Transactional
    public CreateStoreResponseV1 createStore(CustomUserDetailsInfo userDetailsInfo,
                                             CreateStoreRequestV1 request,
                                             MultipartFile storeLogo,
                                             List<MultipartFile> storeImages) throws Exception{

        User user = userDetailsInfo.getUser();

        String logoUrl = null;
        List<String> imageUrls = new ArrayList<>();

        if(storeLogo != null && !storeLogo.isEmpty()){
            logoUrl = s3Util.uploadFile(storeLogo, "store/logo");
        }

        if (storeImages != null && !storeImages.isEmpty()) {
            for (MultipartFile image : storeImages) {
                String url = s3Util.uploadFile(image, "store/image");
                imageUrls.add(url);
            }
        }

        List<StoreImage> images = new ArrayList<>();
        Store store = new Store(
                user,
                request.getStoreName(),
                request.getStoreAddress(),
                logoUrl,
                request.getStorePhone(),
                request.getStoreContent(),
                request.getMinDeliveryPrice(),
                request.getDeliveryTip(),
                request.getOperationHours(),
                request.getDeliveryAddress(),
                images,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        // 대표 이미지 저장
        for (String url : imageUrls) {
            images.add(new StoreImage(store, url));
        }

        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        Store savedStore = storeRepository.save(store);


        return new CreateStoreResponseV1(
                savedStore.getStoreId(),
                savedStore.getStoreName(),
                savedStore.getStoreLogoUrl(),
                firstImageUrl,
                savedStore.getMinDeliveryPrice(),
                savedStore.getDeliveryTip()
        );
    }

    // 가게 주인 입점사 수정
    @Transactional
    public CreateStoreResponseV1 updateStore(CustomUserDetailsInfo userDetailsInfo,
                                             UUID storeId,
                                             CreateStoreRequestV1 request,
                                             MultipartFile storeLogo,
                                             List<MultipartFile> storeImages) throws Exception {

        User user = userDetailsInfo.getUser();

        Store store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        // 권한 체크
        if (!store.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("해당 가게에 대한 수정 권한이 없습니다.");
        }

        // 로고 교체
        if (storeLogo != null && !storeLogo.isEmpty()) {
            // 기존 로고 S3 삭제
            if (store.getStoreLogoUrl() != null) {
                s3Util.deleteFileByUrl(store.getStoreLogoUrl());
            }
            // 업로드 & 저장
            String newLogoUrl = s3Util.uploadFile(storeLogo, "store/logo");
            store.updateStoreLogoUrl(newLogoUrl);
        }

        // 이미지 교체
        if (storeImages != null && !storeImages.isEmpty()) {
            // 기존 이미지 S3 삭제
            if (store.getStoreImages() != null) {
                store.getStoreImages().forEach(img -> {
                    if (img.getStoreImageUrl() != null) {
                        s3Util.deleteFileByUrl(img.getStoreImageUrl());
                    }
                });
            }
            // 연관관계 정리 (orphanRemoval=true 권장)
            store.clearImages();

            // 신규 업로드 & 추가
            for (MultipartFile image : storeImages) {
                if (image != null && !image.isEmpty()) {
                    String url = s3Util.uploadFile(image, "store/image");
                    store.addImage(new StoreImage(store, url));
                }
            }
        }
        // 나머지 속성들 업데이트 (부분 업데이트 정책: request 값으로 덮어쓰기)
        store.update(
                request.getStoreName(),
                request.getStoreAddress(),
                request.getStorePhone(),
                request.getStoreContent(),
                request.getMinDeliveryPrice(),
                request.getDeliveryTip(),
                request.getOperationHours(),
                request.getDeliveryAddress()
        );

        Store updated = storeRepository.save(store);

        String firstImageUrl = updated.getStoreImages() == null || updated.getStoreImages().isEmpty()
                ? null
                : updated.getStoreImages().get(0).getStoreImageUrl();

        return new CreateStoreResponseV1(
                updated.getStoreId(),
                updated.getStoreName(),
                updated.getStoreLogoUrl(),
                firstImageUrl,
                updated.getMinDeliveryPrice(),
                updated.getDeliveryTip()
        );
    }
    // 가게 주인 입점사 삭제
    @Transactional
    public void deleteStoreSoft(CustomUserDetailsInfo userDetailsInfo, UUID storeId) {
        User user = userDetailsInfo.getUser();

        Store store = storeRepository.findByStoreIdAndDeletedDateIsNull(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        if (!store.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("해당 가게에 대한 삭제 권한이 없습니다.");
        }

        // ✅ 소프트 삭제 처리
        store.deleteSoft();
    }
}
