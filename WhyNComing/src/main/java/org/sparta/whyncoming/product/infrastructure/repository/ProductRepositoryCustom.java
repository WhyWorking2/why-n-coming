package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductResponseDto> findAllWithStore();
    List<ProductByCategoryResponseDto> findAllByCategoryName(String categoryName);
}
