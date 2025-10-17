package org.sparta.whyncoming.order.application.service;

import org.sparta.whyncoming.common.security.service.CustomUserDetailsInfo;
import org.sparta.whyncoming.order.domain.entity.OwnerReview;
import org.sparta.whyncoming.order.domain.entity.Review;
import org.sparta.whyncoming.order.domain.repository.OwnerReviewRepository;
import org.sparta.whyncoming.order.domain.repository.ReviewRepository;
import org.sparta.whyncoming.order.presentation.dto.request.CreateOwnerReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.request.UpdateOwnerReviewRequestV1;
import org.sparta.whyncoming.order.presentation.dto.response.CreateOwnerReviewResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReadDetailReviewResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.ReadSimpleReviewResponseV1;
import org.sparta.whyncoming.order.presentation.dto.response.UpdateOwnerReviewResponseV1;
import org.sparta.whyncoming.store.domain.entity.Store;
import org.sparta.whyncoming.store.domain.repository.StoreRepository;
import org.sparta.whyncoming.user.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewOwnerServiceV1 {

    private final ReviewRepository reviewRepository;
    private final OwnerReviewRepository ownerReviewRepository;
    private final StoreRepository storeRepository;

    public ReviewOwnerServiceV1(ReviewRepository reviewRepository, OwnerReviewRepository ownerReviewRepository, StoreRepository storeRepository) {
        this.reviewRepository = reviewRepository;
        this.ownerReviewRepository = ownerReviewRepository;
        this.storeRepository = storeRepository;
    }

    private Store assertOwner(CustomUserDetailsInfo principal, UUID storeId) {
        Store store = storeRepository.findByStoreIdAndDeletedDateIsNull(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        Integer ownerUserNo = store.getUser().getUserNo();
        if (!ownerUserNo.equals(principal.getUserNo())) {
            throw new AccessDeniedException("해당 가게의 소유자만 접근할 수 있습니다.");
        }
        return store;
    }

    public List<ReadSimpleReviewResponseV1> getLatest3Reviews(CustomUserDetailsInfo principal, UUID storeId) {
        assertOwner(principal, storeId);
        List<Review> reviews = reviewRepository.findLatestActiveByStoreId(storeId, PageRequest.of(0, 3));
        return reviews.stream()
                .map(r -> ReadSimpleReviewResponseV1.builder()
                        .reviewRating(r.getReviewRating())
                        .reviewContent(r.getReviewContent())
                        .build())
                .toList();
    }

    public List<ReadDetailReviewResponseV1> getDetailedReviews(CustomUserDetailsInfo principal, UUID storeId) {
        assertOwner(principal, storeId);
        List<Review> reviews = reviewRepository.findDetailedActiveByStoreId(storeId);

        return reviews.stream()
                .map(r -> {
                    OwnerReview orv = r.getOwnerReview();
                    boolean hasOwnerReview = (orv != null && orv.getDeletedDate() == null);
                    return ReadDetailReviewResponseV1.builder()
                            .reviewId(r.getReviewId())
                            .userName(r.getUser().getUserName())
                            .reviewRating(r.getReviewRating())
                            .reviewContent(r.getReviewContent())
                            .reviewPictureUrl(r.getReviewPictureUrl())
                            .ownerReviewId(hasOwnerReview ? orv.getOwnerReviewId() : null)
                            .ownerReviewContent(hasOwnerReview ? orv.getOwnerReviewContent() : null)
                            .ownerReviewCreatedDate(hasOwnerReview ? orv.getCreatedDate() : null)
                            .build();
                })
                .toList();
    }

    @Transactional
    public CreateOwnerReviewResponseV1 createOwnerReview(
            CustomUserDetailsInfo principal,
            UUID storeId,
            UUID reviewId,
            CreateOwnerReviewRequestV1 request
    ) {

        Review review = reviewRepository.findByReviewIdAndStore_StoreId(reviewId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스토어에 리뷰가 없습니다."));

        Integer ownerUserNo = review.getStore().getUser().getUserNo(); // Store.owner(User) 기준
        if (!ownerUserNo.equals(principal.getUserNo())) {
            throw new AccessDeniedException("해당 가게의 소유자만 답변할 수 있습니다.");
        }

        if (ownerReviewRepository.existsByReview_ReviewId(reviewId)) {
            throw new IllegalStateException("이미 사장 답변이 존재합니다.");
        }

        User ownerUser = review.getStore().getUser();
        OwnerReview ownerReview = new OwnerReview(review, ownerUser, request.getOwnerReviewContent());
        OwnerReview saved = ownerReviewRepository.save(ownerReview);

        return CreateOwnerReviewResponseV1.builder()
                .ownerReviewId(saved.getOwnerReviewId())
                .reviewId(review.getReviewId())
                .ownerReviewContent(saved.getOwnerReviewContent())
                .build();
    }

    @Transactional
    public UpdateOwnerReviewResponseV1 updateOwnerReviewById(
            CustomUserDetailsInfo principal,
            UUID storeId,
            UUID reviewId,
            UUID ownerReviewId,
            UpdateOwnerReviewRequestV1 request
    ) {

        OwnerReview ownerReview = ownerReviewRepository.findByIdWithReviewAndStore(ownerReviewId)
                .orElseThrow(() -> new IllegalArgumentException("사장 답변을 찾을 수 없습니다."));

        UUID dbReviewId = ownerReview.getReview().getReviewId();
        UUID dbStoreId  = ownerReview.getReview().getStore().getStoreId();
        if (!dbReviewId.equals(reviewId) || !dbStoreId.equals(storeId)) {
            throw new IllegalArgumentException("요청 경로와 답변이 속한 리뷰/스토어가 일치하지 않습니다.");
        }

        Integer ownerUserNo = ownerReview.getReview().getStore().getUser().getUserNo();
        if (!ownerUserNo.equals(principal.getUserNo())) {
            throw new AccessDeniedException("해당 가게의 소유자만 답변을 수정할 수 있습니다.");
        }

        ownerReview.updateContent(request.getOwnerReviewContent());

        OwnerReview saved = ownerReviewRepository.save(ownerReview);

        return UpdateOwnerReviewResponseV1.builder()
                .ownerReviewId(saved.getOwnerReviewId())
                .reviewId(saved.getReview().getReviewId())
                .ownerReviewContent(saved.getOwnerReviewContent())
                .build();
    }

    @Transactional
    public void deleteOwnerReviewSoft(
            CustomUserDetailsInfo principal,
            UUID storeId,
            UUID reviewId,
            UUID ownerReviewId
    ) {

        OwnerReview ownerReview = ownerReviewRepository.findByIdWithReviewAndStore(ownerReviewId)
                .orElseThrow(() -> new IllegalArgumentException("사장 답변을 찾을 수 없습니다."));

        UUID dbReviewId = ownerReview.getReview().getReviewId();
        UUID dbStoreId  = ownerReview.getReview().getStore().getStoreId();
        if (!dbReviewId.equals(reviewId) || !dbStoreId.equals(storeId)) {
            throw new IllegalArgumentException("요청 경로와 답변이 속한 리뷰/스토어가 일치하지 않습니다.");
        }

        Integer ownerUserNo = ownerReview.getReview().getStore().getUser().getUserNo(); // 또는 getUserNo()
        if (!ownerUserNo.equals(principal.getUserNo())) {
            throw new AccessDeniedException("해당 가게의 소유자만 답변을 삭제할 수 있습니다.");
        }

        if (!ownerReview.isDeleted()) {
            ownerReview.markDeleted();
            ownerReviewRepository.save(ownerReview);
        }
    }
}
