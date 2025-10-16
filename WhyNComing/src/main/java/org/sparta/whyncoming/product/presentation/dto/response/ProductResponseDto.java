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
public class ProductResponseDto {

    private UUID productId;
    private String productName;
    private String productPictureUrl;
    private Integer price;
    private List<String> categoryNameList;
    private Instant createdDate;
    private Instant modifiedDate;
    private Instant deletedDate;
    private String storeName;
    private Integer createdBy;
    private Integer modifiedBy;

    /**
     * 상품 생성 시 사용될 Dto
     * TODO 사용자 권한별로 노출 여부를 구분할 것
     * 일반 사용자에게는 deletedDate가 null일 경우에만 노출
     * 관리자에게는 전부 노툴
     * @param product 가져온 상품 값
     */
    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.productPictureUrl = product.getProductPictureUrl();
        this.categoryNameList = product.getCategoryProducts().stream()
                .map(cp -> cp.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.createdDate = product.getCreatedDate();
        this.modifiedDate = product.getModifiedDate();
        this.deletedDate = product.getDeletedDate();
        this.storeName = product.getStore().getStoreName();
        this.createdBy = product.getCreatedBy();
        this.modifiedBy = product.getModifiedBy();
    }


    public ProductResponseDto(UUID productId, String productName, String storeName, List<String> categoryNameList, Integer price, Instant createdDate, Instant modifiedDate, Instant deletedDate, Integer createdBy, Integer modifiedBy) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName;
        this.price = price;
        this.categoryNameList = categoryNameList;

        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
}
