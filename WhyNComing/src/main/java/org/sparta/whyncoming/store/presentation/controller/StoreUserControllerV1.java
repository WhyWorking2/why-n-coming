package org.sparta.whyncoming.store.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.common.response.ResponseUtil;
import org.sparta.whyncoming.store.application.service.StoreUserServiceV1;
import org.sparta.whyncoming.store.presentation.dto.response.ReadStoreByCategoryResponseV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Category Store", description = "카테고리별 입점사 조회 API")
public class StoreUserControllerV1 {

    private final StoreUserServiceV1 storeUserServiceV1;

    public StoreUserControllerV1(StoreUserServiceV1 storeUserServiceV1) {
        this.storeUserServiceV1 = storeUserServiceV1;
    }

    @Operation(summary = "유저 - 카테고리별 입점사 조회")
    @GetMapping("/{categoryId}/stores")
    public ResponseEntity<ApiResult<List<ReadStoreByCategoryResponseV1>>> getStoresByCategory(
            @PathVariable UUID categoryId
    ) {
        var result = storeUserServiceV1.getStoresByCategory(categoryId);
        return ResponseUtil.success("조회 성공", result);
    }
}
