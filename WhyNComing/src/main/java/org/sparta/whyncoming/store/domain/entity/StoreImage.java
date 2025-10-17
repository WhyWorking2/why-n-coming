package org.sparta.whyncoming.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;

import java.util.UUID;

@Entity
@Table(name = "store_images")
@Getter
@NoArgsConstructor
public class StoreImage extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(columnDefinition = "text", nullable = false)
    private String storeImageUrl;

    public StoreImage(Store store, String storeImageUrl) {
        this.store = store;
        this.storeImageUrl = storeImageUrl;
    }
}
