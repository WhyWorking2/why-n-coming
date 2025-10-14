package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.common.entity.BaseActorEntity;
import org.sparta.whyncoming.user.domain.entity.User;

import java.util.UUID;

@Entity
@Table(name = "owner_reviews")
@Getter
@NoArgsConstructor
public class OwnerReview extends BaseActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ownerReviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Column(length = 255)
    private String ownerReviewContent;

    public OwnerReview(Review review, User user, String ownerReviewContent) {
        this.review = review;
        this.user = user;
        this.ownerReviewContent = ownerReviewContent;
    }
}
