package org.sparta.whyncoming.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.response.ApiResult;
import org.sparta.whyncoming.product.application.service.CategoryService;
import org.sparta.whyncoming.product.presentation.dto.request.CategoryRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 생성
     * TODO 스프링 시큐리티 적용되면 비즈니스 로직 쪽에서 admin 계정인지 검증하는 로직 추가해야함 (http 경로차단을 하면 조회 쪽이 막힘)
     */
    @Operation(summary = "카테고리 추가")
    @PostMapping
    public ResponseEntity<ApiResult<CategoryResponseDto>> createCategory(@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(ApiResult.ofSuccess(response));
    }

    /**
     * 카테고리만 조회
     * @return 카테고리 이름 리스트 전체 조회
     */
    @Operation(summary = "카테고리 전체 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResult.ofSuccess(categories));
    }


    /**
     * 카테고리 삭제
     * @param uuid 삭제될 카테고리의 UUID
     * @return 삭제된 카테고리의 UUID와 품목이름
     */
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResult<CategoryResponseDto>> deleteCategory(@PathVariable UUID uuid) {
        CategoryResponseDto response = categoryService.deleteCategory(uuid);
        return ResponseEntity.ok(ApiResult.ofSuccess("삭제된 상품 정보 :", response));
    }
}


