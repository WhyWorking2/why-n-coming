package org.sparta.whyncoming.product.application.service;

import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.repository.CategoryRepository;
import org.sparta.whyncoming.product.presentation.dto.request.CategoryRequestDto;
import org.sparta.whyncoming.product.presentation.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = categoryRepository.save(new Category(requestDto.getCategoryName()));
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponseDto(
                        category.getCategoryId(),
                        category.getCategoryName(),
                        category.getCreatedDate(),
                        category.getModifiedDate(),
                        category.getDeletedDate()
                )).toList();
    }
}
