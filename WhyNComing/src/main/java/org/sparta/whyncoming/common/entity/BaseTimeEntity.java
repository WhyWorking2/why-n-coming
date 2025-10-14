package org.sparta.whyncoming.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private Instant modifiedDate;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    /** soft delete helpers (선택) */
    public void markDeleted() {
        this.deletedDate = Instant.now();
    }

    public void restore() {
        this.deletedDate = null;
    }

    public boolean isDeleted() {
        return this.deletedDate != null;
    }
}
