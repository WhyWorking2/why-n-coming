package org.sparta.whyncoming.product.infrastructure.repository;


import org.sparta.whyncoming.product.domain.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Override
    public Optional<Product> findByProductId(UUID productId) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAllWithStore() {
        return List.of();
    }
}
