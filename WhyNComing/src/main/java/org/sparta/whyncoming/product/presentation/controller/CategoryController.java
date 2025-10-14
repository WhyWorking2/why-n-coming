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
    @GetMapping
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResult.ofSuccess(categories));
    }
//
//    /**
//     * 단일 카테고리 조회
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable UUID id) {
//        CategoryResponseDto category = categoryService.getCategoryById(id);
//        return ResponseEntity.ok(category);
//    }
//
//    /**
//     * 카테고리 수정
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryResponseDto> updateCategory(
//            @PathVariable UUID id,
//            @RequestBody CategoryRequestDto requestDto
//    ) {
//        CategoryResponseDto updated = categoryService.updateCategory(id, requestDto);
//        return ResponseEntity.ok(updated);
//    }
//
//    /**
//     * 카테고리 삭제
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.noContent().build();
//    }
}


