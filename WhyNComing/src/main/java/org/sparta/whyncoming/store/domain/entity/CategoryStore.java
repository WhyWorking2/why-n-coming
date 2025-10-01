package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Category;
import org.sparta.whyncoming.product.domain.entity.Product;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_category_store")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CategoryStore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryProductSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSeq", nullable = false)
    private Product product;

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
