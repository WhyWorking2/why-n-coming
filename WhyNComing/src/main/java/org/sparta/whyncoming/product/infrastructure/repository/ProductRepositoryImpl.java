package org.sparta.whyncoming.product.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.presentation.dto.response.ProductByCategoryResponseDto;
import org.sparta.whyncoming.product.presentation.dto.response.ProductResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /**
     * 스토어와 카테고리 리스트를 포함한 모든 상품 조
     * @return 반환될 전체 상품 리스트 쿼리
     */
    @Override
    public List<ProductResponseDto> findAllWithStore() {
        // Product + Store + CategoryProducts + Category 모두 fetch
        String jpql = "SELECT DISTINCT p FROM Product p " +
                "JOIN FETCH p.store s " +
                "JOIN FETCH p.categoryProducts cp " +
                "JOIN FETCH cp.category c";
        List<Product> products = em.createQuery(jpql, Product.class)
                .getResultList();

        // ProductId 기준으로 그룹핑 후 CategoryNameList 추출
        Map<UUID, List<String>> categoryMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getProductId,
                        p -> p.getCategoryProducts().stream()
                                .map(cp -> cp.getCategory().getCategoryName())
                                .toList()
                ));

        // DTO 변환
        return products.stream()
                .map(p -> new ProductResponseDto(
                        p.getProductId(),
                        p.getProductName(),
                        p.getStore().getStoreName(),
                        categoryMap.get(p.getProductId()),
                        p.getPrice(),
                        p.getCreatedDate(),
                        p.getModifiedDate(),
                        p.getDeletedDate(),
                        p.getCreatedBy(),
                        p.getModifiedBy()
                ))
                .distinct() // 중복 제거
                .toList();
    }


    /**
     * 카테고리별 상품조회
     * @param categoryName 기준 카테고리
     * @return 반환될 카테고리별 상품 리스트 쿼리
     */
    @Override
    public List<ProductByCategoryResponseDto> findAllByCategoryName(String categoryName) {
        String jpql = "SELECT p FROM Product p " +
                "JOIN FETCH p.store s " +
                "JOIN p.categoryProducts cp " +
                "JOIN cp.category c " +
                "WHERE c.categoryName = :categoryName";

        List<Product> products = em.createQuery(jpql, Product.class)
                .setParameter("categoryName", categoryName)
                .getResultList();

        return products.stream()
                .map(p -> new ProductByCategoryResponseDto(
                        categoryName,
                        p.getProductId(),
                        p.getProductName(),
                        p.getStore().getStoreName(),
                        p.getPrice(),
                        p.getCreatedDate(),
                        p.getModifiedDate(),
                        p.getCreatedBy(),
                        p.getModifiedBy()
                ))
                .distinct() // 필요하면
                .toList();
    }
}
