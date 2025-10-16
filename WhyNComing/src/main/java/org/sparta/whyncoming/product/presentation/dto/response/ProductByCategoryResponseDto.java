package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductByCategoryResponseDto {

    private String categoryName;
    private UUID productId;
    private String productName;
    private Integer price;
    private String storeName;
    private Instant createdDate;
    private Instant modifiedDate;
    private Integer createdBy;
    private Integer modifiedBy;

    /**
     * 상품 조회용 Dto
     * N+1 문제 해결을 위해 Impl에서 매핑 된 값을 매개변수로 함
     * @param categoryName 카테고리명
     * @param productId 상품 uuid
     * @param productName 상품명
     * @param storeName 입점사명
     * @param price 상품가격
     * @param createdDate 상품 추가 시기
     * @param modifiedDate 상품 최종 변경 시기
     * @param createdBy 상품 추가한 유저의 Id
     * @param modifiedBy 상품 최종 수정한 유저의 Id
     */
    public ProductByCategoryResponseDto(String categoryName, UUID productId, String productName, String storeName, Integer price, Instant createdDate, Instant modifiedDate, Integer createdBy, Integer modifiedBy) {
        this.categoryName = categoryName;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.storeName = storeName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
}
