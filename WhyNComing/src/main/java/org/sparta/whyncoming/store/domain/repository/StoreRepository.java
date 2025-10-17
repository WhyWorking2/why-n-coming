package org.sparta.whyncoming.store.domain.repository;

import org.sparta.whyncoming.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        select distinct s
        from Store s
        left join fetch s.storeImages si
        """)
    List<Store> findAllWithImagesForAdmin();

    @Query("""
        select s from Store s
        left join fetch s.storeImages si
        where s.storeId = :storeId
        """)
    Optional<Store> findByIdWithImages(@Param("storeId") UUID storeId);
}
