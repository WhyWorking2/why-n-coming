package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_images")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;

    @Column(columnDefinition = "text", nullable = false)
    private String storeImageUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    public StoreImage(Store store, String storeImageUrl) {
        this.store = store;
        this.storeImageUrl = storeImageUrl;
    }
}
