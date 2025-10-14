package org.sparta.whyncoming.order.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Schema(description = "입점주 주문 상세 조회 응답 DTO")
public class GetStoreOrderDetailResponseV1 {

    @Schema(description = "주문 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID orderId;

    @Schema(description = "주문 항목 리스트")
    private List<OrderItemResponseV1> items;

    @Schema(description = "리뷰 정보")
    private ReviewResponseV1 review;

    @Getter
    @NoArgsConstructor
    @Schema(description = "주문 항목 DTO")
    public static class OrderItemResponseV1 {

        @Schema(description = "아이템 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        private UUID itemId;

        @Schema(description = "상품명", example = "치킨")
        private String name;

        @Schema(description = "수량", example = "2")
        private int quantity;

        public OrderItemResponseV1(UUID itemId, String name, int quantity) {
            this.itemId = itemId;
            this.name = name;
            this.quantity = quantity;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "리뷰 DTO")
    public static class ReviewResponseV1 {

        @Schema(description = "리뷰 ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        private UUID reviewId;

        @Schema(description = "내용", example = "맛있어요!")
        private String content;

        @Schema(description = "평점", example = "5")
        private int rating;

        @Schema(description = "리뷰 이미지 URL", example = "https://cdn.app.com/review1.jpg")
        private String reviewPictureUrl;

        public ReviewResponseV1(UUID reviewId, String content, int rating, String reviewPictureUrl) {
            this.reviewId = reviewId;
            this.content = content;
            this.rating = rating;
            this.reviewPictureUrl = reviewPictureUrl;
        }
    }

    public GetStoreOrderDetailResponseV1(UUID orderId, List<OrderItemResponseV1> items, ReviewResponseV1 review) {
        this.orderId = orderId;
        this.items = items;
        this.review = review;
    }
}