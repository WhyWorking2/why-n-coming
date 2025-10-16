package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Product;

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
     * 카테고리별 상품조회용 Dto
     * @param product 조회된 상품목록
     */
    public ProductByCategoryResponseDto(String categoryName, Product product) {
        this.categoryName = categoryName;
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.storeName = product.getStore().getStoreName();
        this.createdDate = product.getCreatedDate();
        this.modifiedDate = product.getModifiedDate();
        this.createdBy = product.getCreatedBy();
        this.modifiedBy = product.getModifiedBy();
    }
}
