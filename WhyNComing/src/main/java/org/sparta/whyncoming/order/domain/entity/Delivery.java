package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_delivery")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliverySeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user; // 이 부분은 ERD랑 맞지 않아서 수정 필요

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.ACCEPTED; // 기본값 ACCEPTED

    @Column(nullable = false, length = 255)
    private String position;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    public enum DeliveryStatus {
        ACCEPTED,
        COOKED,
        DELIVERING,
        DELIVERED
    }
}
