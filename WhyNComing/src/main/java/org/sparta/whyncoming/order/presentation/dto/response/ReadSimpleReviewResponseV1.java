package org.sparta.whyncoming.order.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadSimpleReviewResponseV1 {

    Integer reviewRating;
    String reviewContent;
}
