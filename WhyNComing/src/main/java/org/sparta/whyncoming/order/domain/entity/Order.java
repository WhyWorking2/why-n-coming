package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.enums.Status;
import org.sparta.whyncoming.product.domain.entity.Cart;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String paymentMethod;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(length = 255)
    private String requests;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SUCCESS; // 기본값 SUCCESS

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    @OneToOne(mappedBy = "order")
    private Review review;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    public Order(Store store, User user, String paymentMethod, Integer totalPrice, String requests,
                 Status status, Review review, List<Cart> carts, Delivery delivery) {
        this.store = store;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.requests = requests;
        this.status = status;
        this.review = review;
        this.carts = carts;
        this.delivery = delivery;
    }
}
