package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
