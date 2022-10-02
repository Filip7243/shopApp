package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    Optional<UserOrder> findByOrderCode(String orderCode);

    Optional<UserOrder> deleteByOrderCode(String orderCode);

    @Override
    @Query("SELECT DISTINCT o FROM UserOrder o")
    List<UserOrder> findAll();

    @Query("SELECT o FROM UserOrder o WHERE o.isDelivered = TRUE")
    List<UserOrder> getDeliveredOrders();
}
