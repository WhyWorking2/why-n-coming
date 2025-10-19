package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductSimpleResponseDto {
    private UUID productId;
    private String productName;

    public ProductSimpleResponseDto(UUID id, String productName) {
        this.productId = id;
        this.productName = productName;
    }
}
