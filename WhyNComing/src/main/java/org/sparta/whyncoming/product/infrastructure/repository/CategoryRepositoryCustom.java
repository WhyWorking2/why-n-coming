package org.sparta.whyncoming.product.infrastructure.repository;


import org.sparta.whyncoming.product.domain.entity.Category;

import java.util.Optional;

public interface CategoryRepositoryCustom {

    Optional<Category> findByCategoryName(String name);
}
