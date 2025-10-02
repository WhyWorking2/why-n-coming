package org.sparta.whyncoming.product.domain.repository;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoy extends JpaRepository<Product, Long> {
}
