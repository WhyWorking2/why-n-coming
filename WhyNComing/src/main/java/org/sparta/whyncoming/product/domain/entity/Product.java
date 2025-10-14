package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.store.domain.entity.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
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
}
