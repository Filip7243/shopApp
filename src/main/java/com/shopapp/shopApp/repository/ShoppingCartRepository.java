package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT s FROM ShoppingCart s JOIN FETCH s.user WHERE s.shoppingCartCode = :#{#shoppingCartCode}")
    Optional<ShoppingCart> findByShoppingCartCode(@Param("shoppingCartCode") String shoppingCartCode);

    @Override
    @Query("SELECT DISTINCT s FROM ShoppingCart s JOIN FETCH s.user")
    List<ShoppingCart> findAll();

    @Override
    @Query("SELECT s FROM ShoppingCart s JOIN FETCH s.user WHERE s.id = :#{#id}")
    Optional<ShoppingCart> findById(@Param("id") Long id);
}
