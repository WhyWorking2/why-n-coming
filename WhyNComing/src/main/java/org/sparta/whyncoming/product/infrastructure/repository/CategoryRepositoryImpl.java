package org.sparta.whyncoming.product.infrastructure.repository;


import org.sparta.whyncoming.product.domain.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    @Override
    public Optional<Category> findByCategoryId(UUID categoryId) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return Optional.empty();
    }

}
