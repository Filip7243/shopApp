package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByShoppingCartCode(String shoppingCartCode);
}
