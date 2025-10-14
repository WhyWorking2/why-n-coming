package org.sparta.whyncoming.product.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    @Schema(description = "카테고리명", example = "퓨전식")
    @NotBlank(message = "카테고리명은 필수입니다.")
    private String categoryName;
}
