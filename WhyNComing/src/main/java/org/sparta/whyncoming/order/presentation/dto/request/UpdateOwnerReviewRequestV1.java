package org.sparta.whyncoming.order.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOwnerReviewRequestV1 {

    @NotBlank(message = "답변 내용을 입력해주세요.")
    private String ownerReviewContent;
}
