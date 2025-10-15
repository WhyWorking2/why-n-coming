package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryCustom {

    Optional<Product> findByProductId(UUID productId);

    /**
     * N+1 문제 해결을 위한 패치조인
     * JPQL 방식이므로 추후 QueryDSL 적용 시 변경 될 예정입니다.
     * @return 스토어 정보가 포함된 상품리스트 반환
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.store")
    List<Product> findAllWithStore();

}
