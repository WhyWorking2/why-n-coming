package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;
import org.sparta.whyncoming.user.domain.entity.User;
@Entity
@Table(name = "category_stores")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CategoryStore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryStoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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


    public CategoryStore(Store store, Category category) {
        this.store = store;
        this.category = category;
    }
}
