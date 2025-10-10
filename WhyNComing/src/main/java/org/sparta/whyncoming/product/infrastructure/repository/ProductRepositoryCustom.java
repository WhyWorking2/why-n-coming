package org.sparta.whyncoming.product.infrastructure.repository;

import org.sparta.whyncoming.product.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryCustom {

    Optional<Product> findByProductSeq(UUID productSeq);
}
