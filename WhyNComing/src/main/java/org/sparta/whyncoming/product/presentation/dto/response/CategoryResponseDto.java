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

    public CategoryResponseDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdDate = category.getCreatedDate();
        this.modifiedDate = category.getModifiedDate();
        this.deletedDate = category.getDeletedDate();
    }

    public CategoryResponseDto(UUID categoryId, String categoryName, Instant createdDate, Instant modifiedDate, Instant deletedDate) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
    }
}
