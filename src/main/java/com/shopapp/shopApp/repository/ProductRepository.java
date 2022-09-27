package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    Boolean existsByProductCode(String productCode);

    @Override
    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.categories")
    List<Product> findAll();

}
