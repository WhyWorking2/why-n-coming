package org.sparta.whyncoming.product.presentation.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private Integer price;
    private String storeName;
    private Instant createdDate;
    private Instant modifiedDate;
    private Instant deletdDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private List<String> categoryNameList;

     /**
     * 삭제된 상품까지 전부 반환할 때 사용하는 Dto
     * @param productId
     * @param productName
     * @param storeName
     * @param categoryNameList
     * @param price
     * @param createdDate
     * @param modifiedDate
     * @param deletedDate
     * @param createdBy
     * @param modifiedBy
     */
    public ProductResponseDto(UUID productId, String productName, String storeName, List<String> categoryNameList, Integer price, Instant createdDate, Instant modifiedDate, Instant deletedDate, Integer createdBy, Integer modifiedBy) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.categoryNameList = categoryNameList;
        this.deletdDate = deletedDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

}
