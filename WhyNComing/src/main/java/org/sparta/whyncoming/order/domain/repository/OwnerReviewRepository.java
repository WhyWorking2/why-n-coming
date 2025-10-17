package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {

    boolean existsByReview_ReviewId(UUID reviewId);
    Optional<OwnerReview> findByReview_ReviewId(UUID reviewId);

    @Query("""
        select orv
        from OwnerReview orv
        join fetch orv.review r
        join fetch r.store s
        where orv.ownerReviewId = :ownerReviewId
        """)
    Optional<OwnerReview> findByIdWithReviewAndStore(UUID ownerReviewId);
}
