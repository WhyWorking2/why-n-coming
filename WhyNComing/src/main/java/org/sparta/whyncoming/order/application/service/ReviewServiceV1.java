package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.domain.repository.ReviewRepository;
import org.sparta.whyncoming.order.presentation.dto.request.CreateReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewStatusResponseV1;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceV1 {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceV1(OrderRepository orderRepository, ReviewRepository reviewRepository) {
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 작성
    @Transactional
    public ReviewStatusResponseV1 writeReview(UUID orderId, CreateReviewRequestV1 req) {
        Order order = findOrderOrThrow(orderId);

        Review review = new Review(
                order.getStore(),
                order.getUser(),
                order,
                req.getRating(),
                req.getContent(),
                req.getReviewPictureUrl(),
                null
        );

        reviewRepository.save(review);
        order.updateReview(review);

        return new ReviewStatusResponseV1("SUCCESS");
    }

    // 공통 조회 메서드
    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
