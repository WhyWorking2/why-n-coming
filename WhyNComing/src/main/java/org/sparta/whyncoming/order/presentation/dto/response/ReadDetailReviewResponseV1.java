package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ReadDetailReviewResponseV1 {

    UUID reviewId;
    String userName;
    Integer reviewRating;
    String reviewContent;
    String reviewPictureUrl;

    // 사장 답변(있을 때만 세팅)
    UUID ownerReviewId;
    String ownerReviewContent;
    Instant ownerReviewCreatedDate;
}
