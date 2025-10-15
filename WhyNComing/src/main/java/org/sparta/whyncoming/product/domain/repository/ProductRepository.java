package org.sparta.whyncoming.product.domain.repository;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.product.infrastructure.repository.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

}
