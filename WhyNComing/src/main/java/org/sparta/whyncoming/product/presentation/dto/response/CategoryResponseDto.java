package org.sparta.whyncoming.product.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.whyncoming.product.domain.entity.Category;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private UUID categoryId;
    private String categoryName;
    private Instant createdDate;
    private Instant modifiedDate;
    private Instant deletedDate;
    private Integer createdBy;
    private Integer modifiedBy;

    /**
     * 반환할 카테고리 정보
     * @param category 새로 저장된 카테고리 정보
     */
    public CategoryResponseDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdDate = category.getCreatedDate();
        this.modifiedDate = category.getModifiedDate();
        this.deletedDate = category.getDeletedDate();
        this.createdBy = category.getCreatedBy();
        this.modifiedBy = category.getModifiedBy();
    }
}
