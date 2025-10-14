package org.sparta.whyncoming.product.infrastructure.repository;


import org.sparta.whyncoming.product.domain.entity.Category;

import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return Optional.empty();
    }
}
