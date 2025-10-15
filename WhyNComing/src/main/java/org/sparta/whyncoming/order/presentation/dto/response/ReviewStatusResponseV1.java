package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 작성 응답 DTO")
public class ReviewStatusResponseV1 {

    @Schema(description = "리뷰 상태", example = "SUCCESS")
    private String status;

    public ReviewStatusResponseV1(String status) {
        this.status = status;
    }
}
