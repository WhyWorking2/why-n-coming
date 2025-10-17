package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateOwnerReviewResponseV1 {

    UUID ownerReviewId;
    UUID reviewId;
    String ownerReviewContent;
}
