package org.sparta.whyncoming.order.presentation.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "리뷰 작성 요청 DTO")
public class CreateReviewRequestV1 {

    @Schema(description = "리뷰 내용", example = "맛있어요!")
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String content;

    @Schema(description = "평점", example = "5")
    @Max(value = 5, message = "평점은 5 이하이어야 합니다.")
    private int rating;

    @Schema(description = "리뷰 이미지 URL 리스트", example = "https://cdn.app.com/review1.jpg")
    private String reviewPictureUrl;
}
