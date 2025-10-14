package org.sparta.whyncoming.product.presentaion.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.CategoryProduct;
import org.sparta.whyncoming.product.domain.entity.Product;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime deletedDate;
    private List<String> categoryNameList;
    private String storeName;

    /**
     * 상품 생성을 위한 생성자 (사장님 권한)
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
//        this.createdDate = product.getCreatedDate();
//        this.modifiedDate = product.getModifiedDate();
//        this.deletedDate = product.getDeletedDate();
    }

    /**
     * TODO 카테고리 리스트에 대한 조회를 어떻게 할지 추가해야함
     * 상품을 리스트 형태로 만들어 조회하기 위한 생성자
     * @param productId 상품의 uuid
     * @param storeName 상품을 팔고있는 가게
     * @param productName 상품이름
     * @param price 상품가격
     * @param productPictureUrl 상품사진
     */
    public ProductResponseDto(UUID productId, String storeName, String productName, Integer price, String productPictureUrl,Instant createdDate, Instant modifiedDate) {
        this.productId = productId;
        this.storeName = storeName;
        this.productName = productName;
        this.price = price;
        this.productPictureUrl = productPictureUrl;
//        this.createdDate = createdDate;
//        this.modifiedDate = modifiedDate;
    }

}
