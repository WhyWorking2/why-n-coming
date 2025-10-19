package org.sparta.whyncoming.store.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.store.application.service.StoreBosServiceV1;
import org.sparta.whyncoming.store.presentation.dto.response.ReadStoreBosResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bos/stores")
@Tag(name = "Store", description = "Store 데이터 API")
public class StoreBosControllerV1 {

    private final StoreBosServiceV1 storeBosServiceV1;

    public StoreBosControllerV1(StoreBosServiceV1 storeBosServiceV1) {
        this.storeBosServiceV1 = storeBosServiceV1;
    }

    @Operation(summary = "관리자의 모든 입점사 조회 (삭제 포함)")
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping
    public ResponseEntity<ApiResult<List<ReadStoreBosResponseV1>>> getStores(
    ) {
        List<ReadStoreBosResponseV1> result = storeBosServiceV1.getStores();
        return ResponseUtil.success("조회 성공", result);
    }

    @Operation(summary = "관리자의 특정 입점사 조회 (삭제 포함)")
    @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResult<ReadStoreBosResponseV1>> getStore(@PathVariable UUID storeId) {
        ReadStoreBosResponseV1 result = storeBosServiceV1.getStore(storeId);
        return ResponseUtil.success("조회 성공", result);
    }

}
