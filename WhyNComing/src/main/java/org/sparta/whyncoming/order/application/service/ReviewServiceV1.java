package org.sparta.whyncoming.order.application.service;

import jakarta.transaction.Transactional;
import org.sparta.whyncoming.common.exception.BusinessException;
import org.sparta.whyncoming.common.exception.ErrorCode;
import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.order.domain.entity.Order;
import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.repository.OrderRepository;
import org.sparta.whyncoming.order.domain.repository.ReviewRepository;
import org.sparta.whyncoming.order.presentation.dto.request.CreateReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewStatusResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReviewWithReplyResponseDtoV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * GET /member/reviews
     * 내가 쓴 리뷰 + 사장님 답글을 함께 조회 (페이지당 3건)
     */
    @Transactional
    public Page<ReviewWithReplyResponseDtoV1>
    getMyReviewsWithOwnerReplies(CustomUserDetailsInfo userDetailsInfo,
                                 Pageable pageable) {

        // 기본 구현: 사용자 리뷰를 페이지로 조회 후, OwnerReview를 매핑 (N+1 방지 위해 추후 전용 쿼리로 최적화 가능)
        Page<Review> page = reviewRepository.findByUserAndDeletedDateIsNull(userDetailsInfo.getUser(), pageable);

        return page.map(r -> {
            OwnerReview orv = r.getOwnerReview();
            // 가게/상품 메타가 필요하다면 엔티티에 맞춰 null-safe로 꺼냄
            UUID storeId = (r.getStore() != null) ? r.getStore().getStoreId() : null;
            String storeName = (r.getStore() != null) ? r.getStore().getStoreName() : null;

            return new ReviewWithReplyResponseDtoV1(
                    r.getReviewId(),
                    r.getReviewContent(),
                    r.getCreatedDate(),
                    storeId,
                    storeName,
                    (orv != null ? orv.getOwnerReviewId() : null),
                    (orv != null ? orv.getOwnerReviewContent() : null),
                    (orv != null ? orv.getCreatedDate() : null)
            );
        });
    }


    /**
     * DELETE /member/reviews/{reviewId}
     * 내가 쓴 특정 리뷰 삭제
     */
    @Transactional
    public void deleteMyReview(CustomUserDetailsInfo userDetailsInfo, UUID reviewId) {
        if (reviewId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "리뷰 ID가 필요합니다.");
        }

        // 1) 리뷰 로드
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "삭제할 리뷰를 찾을 수 없습니다."));

        // 2) 소유권 체크 (프록시 안전하게 식별자 비교)
        Integer reviewUserNo = (review.getUser() != null) ? review.getUser().getUserNo() : null;
        Integer currentUserNo = (userDetailsInfo != null && userDetailsInfo.getUser() != null)
                ? userDetailsInfo.getUser().getUserNo()
                : null;

        if (reviewUserNo == null || currentUserNo == null || !reviewUserNo.equals(currentUserNo)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        // 3) 이미 삭제된 경우는 무시(idempotent)
        if (review.getDeletedDate() != null) {
            return;
        }

        // 4) 리뷰 소프트 딜리트 (감사 컬럼 채움)
        review.markDeleted(); // 아래 엔티티 메서드 참고

        // 5) 사장답글도 함께 소프트 딜리트(있다면, 정책에 따라)
        OwnerReview ownerReview = review.getOwnerReview();
        if (ownerReview != null && ownerReview.getDeletedDate() == null) {
            ownerReview.markDeleted();
        }

        // 6) flush/save 호출 불필요: 영속 상태 변경이므로 트랜잭션 커밋 시점에 업데이트됨
    }

}
