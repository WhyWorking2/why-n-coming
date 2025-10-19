package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductActiveResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductSimpleResponseDto;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductResponseDto> findAllWithStore();
    List<ProductActiveResponseDto> findAllActiveWithStore();
    List<ProductByCategoryResponseDto> findAllByCategoryName(String categoryName);
    List<ProductSimpleResponseDto> findAllSimple();
}
