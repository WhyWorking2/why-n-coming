package org.sparta.whyncoming.product.infrastructure.repository;


import org.sparta.whyncoming.product.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Override
    public Optional<Product> findByProductId(UUID productId) {
        return Optional.empty();
    }
}
