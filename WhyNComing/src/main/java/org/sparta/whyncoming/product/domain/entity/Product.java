package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "text")
    private String productPictureUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

    public Product(Store store, String productName, String description, Integer price, String productPictureUrl,
                   List<Cart> carts, List<CategoryProduct> categoryProducts) {
        this.store = store;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.productPictureUrl = productPictureUrl;
        this.carts = carts;
        this.categoryProducts = categoryProducts;
    }

    /**
     * 상품 생성 시 사용되는 생성자 (cart 부분에 대한 것 어떻게 할지 수정 필요)
     * @param store 가게정보
     * @param productName 상품 이름
     * @param description 상품 설명
     * @param price 상품 가격
     * @param productPictureUrl 상품 사진
     * @param categoryProductList 상품이 속한 카테고리의 목록
     */
    public Product(Store store, String productName, String description, Integer price, String productPictureUrl, List<CategoryProduct> categoryProductList) {
        this.store = store;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.productPictureUrl = productPictureUrl;
        this.categoryProducts = categoryProductList;
    }

    public void update(String productName, Integer price, String description, String productPictureUrl) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.productPictureUrl = productPictureUrl;
    }

}
