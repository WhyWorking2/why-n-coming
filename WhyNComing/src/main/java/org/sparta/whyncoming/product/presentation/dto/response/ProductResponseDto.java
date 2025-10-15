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
    private Integer price;
    private String description;
    private String productPictureUrl;
    private Instant createdDate;
    private Instant modifiedDate;
    private Instant deletedDate;
    private List<String> categoryNameList;
    private String storeName;

    /**
     * 상품 관련 전체 Dto
     * @param product 생성된 값에 대한 Response 값 반환
     */
    public ProductResponseDto(Product product) {
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
        this.deletedDate = product.getDeletedDate();
        this.storeName = product.getStore().getStoreName();
    }
}
