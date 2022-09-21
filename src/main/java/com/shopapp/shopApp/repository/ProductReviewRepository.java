package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    Optional<ProductReview> findByUser(AppUser user);
    Optional<ProductReview> findByReviewCode(String reviewCode);
    List<ProductReviewAddUpdateDto> getProductReviewByUserOrderByStarsDesc(AppUser user);
}
