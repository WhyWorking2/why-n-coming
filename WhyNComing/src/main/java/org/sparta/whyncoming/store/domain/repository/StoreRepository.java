package org.sparta.whyncoming.store.domain.repository;

import org.sparta.whyncoming.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreName(String storeName);
    Optional<Store> findByStoreId(UUID storeId);
    List<Store> findAllByDeletedDateIsNull();
    Optional<Store> findByStoreIdAndDeletedDateIsNull(UUID storeId);

    List<Store> findAllByUser_UserNoAndDeletedDateIsNull(Integer userNo);
    Optional<Store> findByStoreIdAndUser_UserNoAndDeletedDateIsNull(UUID storeId, Integer userId);

    // 관리자 전용 (삭제 포함)
    @Query("SELECT s FROM Store s")
    List<Store> findAllIncludingDeleted();

    @Query("SELECT s FROM Store s WHERE s.storeId = :storeId")
    Optional<Store> findAnyById(UUID storeId);
}
