package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.domain.entity.Category;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryCustom {

    Optional<Category> findByCategoryId(UUID categoryId);

    Optional<Category> findByCategoryName(String name);

}
