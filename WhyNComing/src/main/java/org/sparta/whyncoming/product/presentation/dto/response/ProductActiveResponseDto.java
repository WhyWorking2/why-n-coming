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
public class ProductActiveResponseDto {

    private UUID productId;
    private String productName;
    private String storeName;
    private String productPictureUrl;
    private Integer price;
    private Instant createdDate;
    private Instant modifiedDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private List<String> categoryNameList;

    /**
     * 상품 생성 시 사용될 Dto
     * TODO 사용자 권한별로 노출 여부를 구분할 것
     * 일반 사용자에게는 deletedDate가 null일 경우에만 노출
     * 관리자에게는 전부 노툴
     * @param product 가져온 상품 값
     */
    public ProductActiveResponseDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.productPictureUrl = product.getProductPictureUrl();
        this.categoryNameList = product.getCategoryProducts().stream()
                .map(cp -> cp.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.createdDate = product.getCreatedDate();
        this.modifiedDate = product.getModifiedDate();
        this.storeName = product.getStore().getStoreName();
        this.createdBy = product.getCreatedBy();
        this.modifiedBy = product.getModifiedBy();
    }

    /**
     * 상품 전체 조회에 사용될 메서드 (삭제된 상품 제외)
     * @param productId 조회될 상품 uuid
     * @param productName 조회될 상품명
     * @param storeName 조회될 상품의 입점사
     * @param categoryNameList 조회될 상품의 카테고리 리스트
     * @param price 조회될 상품의 가격
     * @param createdDate 조회될 상품의 생성시간
     * @param modifiedDate 수정시간
     * @param createdBy 상품 생성자
     * @param modifiedBy 상품의 마지막 수정자
     */
    public ProductActiveResponseDto(UUID productId, String productName, String storeName, String productPictureUrl, List<String> categoryNameList, Integer price, Instant createdDate, Instant modifiedDate, Integer createdBy, Integer modifiedBy) {
        this.productId = productId;
        this.productName = productName;
        this.storeName = storeName;
        this.productPictureUrl = productPictureUrl;
        this.price = price;
        this.categoryNameList = categoryNameList;

        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
}
