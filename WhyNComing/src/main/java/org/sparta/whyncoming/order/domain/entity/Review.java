package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;

import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
public class Review extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer reviewRating;

    @Column(length = 255)
    private String reviewContent;

    @Column(columnDefinition = "text")
    private String reviewPictureUrl;

    @OneToOne(mappedBy = "review")
    private OwnerReview ownerReview;

    public Review(Store store, User user, Order order, Integer reviewRating,
                  String reviewContent, String reviewPictureUrl, OwnerReview ownerReview) {
        this.store = store;
        this.user = user;
        this.order = order;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewPictureUrl = reviewPictureUrl;
        this.ownerReview = ownerReview;
    }
}
