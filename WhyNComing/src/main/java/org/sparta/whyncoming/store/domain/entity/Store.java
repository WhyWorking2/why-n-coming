package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
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
    private BigDecimal storeRating = BigDecimal.ZERO;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;


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

    public Store(User user, String storeName, String storeAddress, String storeLogoUrl, String storePhone,
                 String storeContent, Integer minDeliveryPrice, Integer deliveryTip, String operationHours, String deliveryAddress,
                 List<StoreImage> storeImages, List<Product> products, List<Order> orders,
                 List<Review> reviews, List<Cart> carts, List<CategoryStore> categoryStores) {
        this.user = user;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeLogoUrl = storeLogoUrl;
        this.storePhone = storePhone;
        this.storeContent = storeContent;
        this.minDeliveryPrice = minDeliveryPrice;
        this.deliveryTip = deliveryTip;
        this.operationHours = operationHours;
        this.deliveryAddress = deliveryAddress;
        this.storeImages = storeImages;
        this.products = products;
        this.orders = orders;
        this.reviews = reviews;
        this.carts = carts;
        this.categoryStores = categoryStores;
    }
}
