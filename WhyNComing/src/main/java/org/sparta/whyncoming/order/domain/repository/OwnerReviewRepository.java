package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, UUID> {

}
