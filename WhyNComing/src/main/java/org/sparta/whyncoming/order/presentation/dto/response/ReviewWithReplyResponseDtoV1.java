package org.sparta.whyncoming.order.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReviewWithReplyResponseDtoV1(
        UUID reviewId,
        String reviewContent,
        Instant reviewCreatedDate,
        // 가게/상품 등 메타
        UUID storeId,
        String storeName,
        // 사장님 답글 (없을 수 있음)
        UUID ownerReviewId,
        String ownerReplyContent,
        Instant ownerReplyCreatedDate
) {}
