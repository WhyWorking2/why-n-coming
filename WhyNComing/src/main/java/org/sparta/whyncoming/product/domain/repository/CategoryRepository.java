package org.sparta.whyncoming.product.domain.repository;

import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.infrastructure.repository.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> , CategoryRepositoryCustom {

}
