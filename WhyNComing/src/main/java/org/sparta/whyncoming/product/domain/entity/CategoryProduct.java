package org.sparta.whyncoming.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_category_product")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CategoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryStoreSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeSeq", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorySeq", nullable = false)
    private Category category;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;
}
