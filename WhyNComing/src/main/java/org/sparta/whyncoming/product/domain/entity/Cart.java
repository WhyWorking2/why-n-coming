package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_cart")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeSeq", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSeq", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderSeq", nullable = true) // ERD 상 Order가 저장되어 있지 않아도 가능한 것으로 봄
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer quantity = 1;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;
}
