package org.sparta.whyncoming.product.domain.repository;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.infrastructure.repository.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByProductId(UUID productId);

}
