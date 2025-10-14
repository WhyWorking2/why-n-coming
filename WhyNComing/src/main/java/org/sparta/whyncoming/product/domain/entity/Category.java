package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.store.domain.entity.CategoryStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class Category extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;

    @Column(name = "category_name", length = 20, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryStore> categoryStores = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryProduct> categoryProducts = new ArrayList<>();

    public Category(String categoryName, List<CategoryStore> categoryStores, List<CategoryProduct> categoryProducts) {
        this.categoryName = categoryName;
        this.categoryStores = categoryStores;
        this.categoryProducts = categoryProducts;
    }
}
