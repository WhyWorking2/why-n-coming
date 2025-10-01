package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.product.domain.entity.Cart;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_store")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String storeName;

    @Column(nullable = false, length = 255)
    private String storeAddress;

    @Column(columnDefinition = "text")
    private String storeLogoUrl;

    @Column(nullable = false, length = 20, unique = true)
    private String storePhone;

    @Column(length = 255)
    private String storeContent;

    @Column(nullable = false)
    private Integer minDeliveryPrice = 0;

    @Column(nullable = false)
    private Integer deliveryTip = 0;

    @Column(nullable = false, precision = 2, scale = 1)
    private Double storeRating = 0.0;

    @Column(nullable = false)
    private Integer storeReviewCount = 0;

    @Column(length = 255)
    private String operationHours;

    @Column(length = 255)
    private String deliveryAddress;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryStore> categoryStores = new ArrayList<>();
}