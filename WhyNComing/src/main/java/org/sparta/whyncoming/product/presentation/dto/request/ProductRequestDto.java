package org.sparta.whyncoming.product.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

    @Schema(description = "상품명", example = "마라탕")
    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @Schema(description = "가격", example = "12000")
    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @Schema(description = "상품 설명", example = "얼얼하고 매운 마라탕")
    private String description;

    @Schema(description = "상품 이미지 URL", example = "https://example.com/images/malatang.jpg")
    private String productPictureUrl;

    @Schema(description = "가게 이름", example = "중국집1")
    private String storeName;

    @Schema(description = "카테고리 이름 목록", example = "[\"중식\"]")
    private List<String> categoryNames;
}
