package org.sparta.whyncoming.product.presentaion.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

    private UUID productSeq;
    private String name;
    private Integer price;
    private String description;
    private String productPictureUrl;
    private LocalDateTime createdDate;
    private List<String> categoryNameList;

    public ProductResponseDto(Product product) {
        this.productSeq = product.getProductSeq();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.categoryNameList = product.getCategoryProducts().stream()
                .map(cp -> cp.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.productPictureUrl = product.getProductPictureUrl();
        this.createdDate = product.getCreatedDate();
    }
}
