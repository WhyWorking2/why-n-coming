package org.sparta.whyncoming.order.domain.repository;

import org.sparta.whyncoming.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUser_UserNo(Integer userNo);
    List<Order> findAllByStore_StoreId(UUID storeId);
}
