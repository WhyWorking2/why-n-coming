package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.domain.entity.Category;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryCustom {

    Optional<Category> findByCategoryId(UUID categoryId);

    List<Category> findAllByCategoryNameIn(Collection<String> categoryNames);
}
