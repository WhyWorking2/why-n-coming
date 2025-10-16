package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Product;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProductDetailResponseDto {

    private UUID productId;
    private String productName;
    private Integer price;
    private String description;
    private String productPictureUrl;
    private List<String> categoryNameList;
    private Instant createdDate;
    private Instant modifiedDate;
    private String storeName;
    private Integer createdBy;
    private Integer modifiedBy;


    /**
     * 상품 상세 조회 시 사용될 Dto
     * 상품 상세 조회 시는 무조건 삭제된 값이 조회되지 않게(손님용)으로 개발
     * @param product 상세 조회할 상품 product
     */
    public ProductDetailResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.categoryNameList = product.getCategoryProducts().stream()
                .map(cp -> cp.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.productPictureUrl = product.getProductPictureUrl();
        this.createdDate = product.getCreatedDate();
        this.modifiedDate = product.getModifiedDate();
        this.createdBy = product.getCreatedBy();
        this.modifiedBy = product.getModifiedBy();
    }
}
