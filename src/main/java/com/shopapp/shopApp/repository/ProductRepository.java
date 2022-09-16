package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCategory(Category category);
    Optional<Product> findByProductCode(String productCode);
    Boolean existsByName(String name);
    Boolean existsByProductCode(String productCode);
}
