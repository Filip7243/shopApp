package com.shopapp.shopApp.service.product;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ProductReview;

import java.util.List;

public interface ProductReviewService {

    List<ProductReviewAddUpdateDto> getUserReviews(AppUser user);
    void addReview(ProductReview review);

    void updateReview(String reviewCode, ProductReviewAddUpdateDto review);

    void deleteReview(String reviewCode);

}
