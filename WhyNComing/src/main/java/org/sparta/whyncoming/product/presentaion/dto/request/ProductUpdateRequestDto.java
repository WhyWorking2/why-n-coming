package org.sparta.whyncoming.product.presentaion.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {

    @Schema(description = "상품명", example = "탕수육")
    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @Schema(description = "가격", example = "18000")
    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @Schema(description = "상품 설명", example = "고소하고 바삭한 탕수육")
    private String description;

    @Schema(description = "상품 이미지 URL", example = "https://example.com/images/tang.jpg")
    private String productPictureUrl;

}
