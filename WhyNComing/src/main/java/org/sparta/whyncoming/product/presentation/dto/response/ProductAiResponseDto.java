package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductAiResponseDto {

    private boolean success;
    private String message;
    private String aiRecommendation;
    private List<ProductSimpleResponseDto> matchedProducts;

    public ProductAiResponseDto(
            boolean success,
            String message,
            String aiRecommendation,
            List<ProductSimpleResponseDto> matchedProducts
    ) {
        this.success = success;
        this.message = message;
        this.aiRecommendation = aiRecommendation;
        this.matchedProducts = matchedProducts;
    }

    public static ProductAiResponseDto ofError(String message) {
        return new ProductAiResponseDto(false, message, null, List.of());
    }

    public static ProductAiResponseDto ofSuccess(String aiRecommendation, List<ProductSimpleResponseDto> products) {
        return new ProductAiResponseDto(true, "AI 추천 성공", aiRecommendation, products);
    }
}
