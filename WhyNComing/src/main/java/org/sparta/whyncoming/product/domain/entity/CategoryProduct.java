package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;

import java.util.UUID;

@Entity
@Table(name = "category_products")
@Getter
@NoArgsConstructor
public class CategoryProduct extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public CategoryProduct(Product product, Category category) {
        this.product = product;
        this.category = category;
    }
}
