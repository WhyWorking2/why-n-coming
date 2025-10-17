package org.sparta.whyncoming.product.domain.repository;

import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.infrastructure.repository.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> , CategoryRepositoryCustom {

    Optional<Category> findByCategoryId(UUID categoryId);
    List<Category> findAllByCategoryNameIn(Collection<String> categoryNames);

}
