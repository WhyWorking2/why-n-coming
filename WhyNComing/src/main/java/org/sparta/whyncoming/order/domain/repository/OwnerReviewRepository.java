package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.sparta.whyncoming.order.infrastructure.repository.OwnerReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long>, OwnerReviewRepositoryCustom {
}
