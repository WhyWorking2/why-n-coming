package org.sparta.whyncoming.product.application.service;

import org.sparta.whyncoming.common.response.ErrorCode;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.presentation.dto.request.CategoryRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 카테고리 생성
     * @param requestDto 카테고리명
     * @return 카테고리Id와 카테고리명
     */
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = categoryRepository.save(new Category(requestDto.getCategoryName()));
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::new).toList();
    }

    public CategoryResponseDto deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(ErrorCode.NOT_FOUND)));

        category.delete();

        return new CategoryResponseDto(category);
    }
}
