package org.sparta.whyncoming.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_review")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeSeq", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderSeq", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer reviewRating;

    @Column(length = 255)
    private String reviewContent;

    @Column(columnDefinition = "text")
    private String reviewPictureUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column
    private LocalDateTime deletedDate;

    @OneToOne(mappedBy = "review")
    private OwnerReview ownerReview;
}
