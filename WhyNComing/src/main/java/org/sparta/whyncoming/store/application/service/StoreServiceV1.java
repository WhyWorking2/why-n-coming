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

@Service
public class StoreServiceV1 {

    private final StoreRepository storeRepository;
    private final S3Util s3Util;


    public StoreServiceV1(StoreRepository storeRepository, S3Util s3Util) {
        this.storeRepository = storeRepository;
        this.s3Util = s3Util;
    }

    // 가게 주인 입점사 조회

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

    // 가게 주인 입점사 삭제
}
