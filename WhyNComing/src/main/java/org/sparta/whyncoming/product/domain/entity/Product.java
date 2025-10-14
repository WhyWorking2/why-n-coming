package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.sparta.whyncoming.order.domain.entity.Cart;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.store.domain.entity.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "products")
@Getter
@DynamicUpdate //삭제시간 추가를 위한 동적 업데이트 어노테이션 추가
@NoArgsConstructor
public class Product extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "text")
    private String productPictureUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

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

    /**
     * 상품 수정시 사용되는 생성자
     * @param productName 상품명
     * @param price 가격
     * @param description 설명
     * @param productPictureUrl 이미지
     */
    public void update(String productName, Integer price, String description, String productPictureUrl) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.productPictureUrl = productPictureUrl;
    }

    public void delete() {
        log.info("Product.delete() called - deletedDate set to {}", this.getDeletedDate());
    }
}
