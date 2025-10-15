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
     */
    @Operation(summary = "카테고리 추가")
    @PostMapping
    public ResponseEntity<ApiResult<CategoryResponseDto>> createCategory(@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(ApiResult.ofSuccess(response));
    }

    /**
     * 카테고리 전체 조회
     */
    @Operation(summary = "카테고리 전체 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResult.ofSuccess(categories));
    }



    /**
     * TODO 카테고리 수정에 대한 의견 - 카테고리 수정은 하면 안된다는 생각에서 제외할까 합니다.
     * 카테고리 삭제
     */
    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResult<String>> deleteCategory(@PathVariable UUID uuid) {
        String categoryId = categoryService.deleteCategory(uuid);
        return ResponseEntity.ok(ApiResult.ofSuccess("삭제된 상품의 UUID :", categoryId));
    }
}


