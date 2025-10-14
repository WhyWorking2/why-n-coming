package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.order.domain.enums.Status;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
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

    public void pay(String method, String requests) {
        if (this.status != Status.CREATED) {
            throw new IllegalStateException("이 상태에서는 결제를 할 수 없습니다.");
        }

        this.paymentMethod = method;
        this.requests = requests;
        this.status = Status.SUCCESS;
    }

    public void assignDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
