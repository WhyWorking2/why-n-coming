package org.sparta.whyncoming.product.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.whyncoming.product.application.service.CategoryService;
import org.sparta.whyncoming.product.presentation.dto.request.CategoryRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(response);
    }

//    /**
//     * 카테고리 전체 조회
//     */
//    @GetMapping
//    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
//        List<CategoryResponseDto> categories = categoryService.getAllCategories();
//        return ResponseEntity.ok(categories);
//    }
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

