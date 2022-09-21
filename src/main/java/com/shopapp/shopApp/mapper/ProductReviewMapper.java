package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.model.ProductReview;

import java.util.UUID;

public class ProductReviewMapper {

    public static ProductReview mapToProductReview(ProductReviewAddUpdateDto reviewDto) {
        return ProductReview.builder()
                .reviewCode(UUID.randomUUID().toString())
                .topic(reviewDto.getTopic())
                .description(reviewDto.getDescription())
                .stars(reviewDto.getStars())
                .product(null)
                .user(null)
                .build();
    }
}
