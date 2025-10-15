package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryCustom {

    Optional<Product> findByProductId(UUID productId);

    /**
     * 상품 전체 조회
     * @return 스토어 정보가 포함된 상품리스트 반환
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.store")
    List<Product> findAllWithStore();

    /**
     * 카테고리별 상품 조회
     * @param categoryName 조건이 될 카테고리명
     * @return 카테고리별 상품 리스트 조회
     */
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.categoryProducts cp " +
            "JOIN FETCH p.store s " +
            "WHERE cp.category.categoryName = :categoryName")
    List<Product> findAllByCategoryName(@Param("categoryName") String categoryName);


}
