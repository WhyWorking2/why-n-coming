package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.sparta.whyncoming.common.entity.BaseActorEntity;

import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;

import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor
public class Cart extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true) // ERD 상 Order가 저장되어 있지 않아도 가능한 것으로 봄
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer quantity = 1;

    public Cart(Store store, Product product, Order order, User user, Integer quantity) {
        this.store = store;
        this.product = product;
        this.order = order;
        this.user = user;
        this.quantity = quantity;
    }

    public void updateQuantity(Integer quantity){
        this.quantity = quantity;
    }
}
