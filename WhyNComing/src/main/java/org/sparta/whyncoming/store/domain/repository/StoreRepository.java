package org.sparta.whyncoming.store.domain.repository;

import org.sparta.whyncoming.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreName(String storeName);
    Optional<Store> findByStoreId(UUID storeId);
}
