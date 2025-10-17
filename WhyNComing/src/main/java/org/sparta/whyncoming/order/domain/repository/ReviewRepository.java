package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReviewIdAndStore_StoreId(UUID reviewId, UUID storeId);

    @Query("""
        select r
        from Review r
        where r.store.storeId = :storeId
          and r.deletedDate is null
        order by r.createdDate desc
        """)
    List<Review> findLatestActiveByStoreId(@Param("storeId") UUID storeId, Pageable pageable);

    @Query("""
        select r
        from Review r
        join fetch r.user u
        left join fetch r.ownerReview orv
        where r.store.storeId = :storeId
          and r.deletedDate is null
        order by r.createdDate desc
        """)
    List<Review> findDetailedActiveByStoreId(UUID storeId);
}
