package org.sparta.whyncoming.user.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.order.domain.entity.Delivery;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@NoArgsConstructor
public class Address extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 1)
    private String representativeYn = "N";  // 기본값 N


    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    public Address(User user, String address, String representativeYn, List<Delivery> deliveries) {
        this.user = user;
        this.address = address;
        this.representativeYn = representativeYn;
        this.deliveries = deliveries;
    }
}
