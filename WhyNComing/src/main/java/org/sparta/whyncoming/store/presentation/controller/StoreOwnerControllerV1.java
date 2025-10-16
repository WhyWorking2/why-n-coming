package org.sparta.whyncoming.store.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.store.application.service.StoreOwnerServiceV1;
import org.sparta.whyncoming.store.presentation.dto.request.CreateStoreOwnerRequestV1;
import org.sparta.whyncoming.store.presentation.dto.response.CreateStoreOwnerResponseV1;
import org.sparta.whyncoming.store.presentation.dto.response.ReadStoreOwnerResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/stores")
@Tag(name = "Store", description = "Store 데이터 API")
public class StoreOwnerControllerV1 {

    private final StoreOwnerServiceV1 storeOwnerServiceV1;

    public StoreOwnerControllerV1(StoreOwnerServiceV1 storeOwnerServiceV1) {
        this.storeOwnerServiceV1 = storeOwnerServiceV1;
    }

    @Operation(summary = "사장 본인의 모든 입점사 조회 (삭제 제외)")
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping()
    public ResponseEntity<ApiResult<List<ReadStoreOwnerResponseV1>>> getMyStores(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo
    ) {
        List<ReadStoreOwnerResponseV1> result = storeOwnerServiceV1.getMyStores(userDetailsInfo);
        return ResponseUtil.success("조회 성공", result);
    }

    @Operation(summary = "사장 본인의 특정 입점사 상세 조회 (삭제 제외)")
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResult<ReadStoreOwnerResponseV1>> getMyStoreDetail(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable UUID storeId
    ) {
        ReadStoreOwnerResponseV1 result = storeOwnerServiceV1.getMyStoreDetail(userDetailsInfo, storeId);
        return ResponseUtil.success("조회 성공", result);
    }

    @Operation(summary = "가게 주인 입점사 추가")
    @PreAuthorize( "hasRole('OWNER')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResult<CreateStoreOwnerResponseV1>> createStore(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @Valid @RequestPart("request") CreateStoreOwnerRequestV1 request,
            @RequestPart(value = "storeLogo", required = false) MultipartFile storeLogo,
            @RequestPart(value = "storeImage", required = false) List<MultipartFile> storeImages
    )throws Exception {

        CreateStoreOwnerResponseV1 response = storeOwnerServiceV1.createStore(userDetailsInfo, request, storeLogo, storeImages);
        return ResponseUtil.success("생성 성공", response);
    }

    @Operation(summary = "가게 주인 입점사 수정")
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping(value = "/{storeId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResult<CreateStoreOwnerResponseV1>> updateStore(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable UUID storeId,
            @Valid @RequestPart("request") CreateStoreOwnerRequestV1 request,
            @RequestPart(value = "storeLogo", required = false) MultipartFile storeLogo,
            @RequestPart(value = "storeImages", required = false) List<MultipartFile> storeImages
    ) throws Exception {
        CreateStoreOwnerResponseV1 response = storeOwnerServiceV1.updateStore(
                userDetailsInfo, storeId, request, storeLogo, storeImages
        );
        return ResponseUtil.success("수정 성공", response);
    }

    @Operation(summary = "가게 주인 입점사 소프트 삭제")
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResult<Void>> deleteStoreSoft(
            @AuthenticationPrincipal CustomUserDetailsInfo userDetailsInfo,
            @PathVariable UUID storeId
    ) {
        storeOwnerServiceV1.deleteStoreSoft(userDetailsInfo, storeId);
        return ResponseUtil.success("입점사 소프트 삭제 완료", null);
    }
}
