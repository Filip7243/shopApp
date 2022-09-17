package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProduct(Product product);
}
