package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.order.domain.enums.DeliveryStatus;
import org.sparta.whyncoming.user.domain.entity.Address;
import org.sparta.whyncoming.user.domain.entity.User;


import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor
public class Delivery extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.ACCEPTED; // 기본값 ACCEPTED

    @Column(nullable = false, length = 255)
    private String deliveryPosition;


    public Delivery(Order order, Address address, User user, DeliveryStatus deliveryStatus, String deliveryPosition) {
        this.order = order;
        this.address = address;
        this.user = user;
        this.deliveryStatus = deliveryStatus;
        this.deliveryPosition = deliveryPosition;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
