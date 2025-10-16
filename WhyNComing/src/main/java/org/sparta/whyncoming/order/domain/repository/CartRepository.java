package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findAllByUser_UserNo(Integer userNo);

    Optional<Cart> findByUserAndProduct(User user, Product product);
}
