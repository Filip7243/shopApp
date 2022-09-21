package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.ProductReview;

public interface ProductReviewService {

    void addReview(ProductReview review);
    void updateReview(String reviewCode, ProductReview review);
    void deleteReview(String reviewCode);
}
