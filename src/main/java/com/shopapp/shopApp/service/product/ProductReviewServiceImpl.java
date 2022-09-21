package com.shopapp.shopApp.service.product;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.exception.product.ProductReviewNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ProductReview;
import com.shopapp.shopApp.repository.ProductReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shopapp.shopApp.constants.ExceptionsConstants.REVIEW_NOT_FOUND;

@Service
@AllArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;

    @Override
    public List<ProductReviewAddUpdateDto> getUserReviews(AppUser user) {
        return reviewRepository.getProductReviewByUserOrderByStarsDesc(user);
    }

    @Override
    public void addReview(ProductReview review) {
        reviewRepository.save(review);
    }

    @Override
    public void updateReview(String reviewCode, ProductReviewAddUpdateDto review) {
        ProductReview productReview = getReview(reviewCode);
        productReview.setTopic(review.getTopic());
        productReview.setDescription(review.getDescription());
        productReview.setStars(review.getStars());

        reviewRepository.save(productReview);
    }

    @Override
    public void deleteReview(String reviewCode) {
        ProductReview productReview = getReview(reviewCode);
        reviewRepository.delete(productReview);
    }

    private ProductReview getReview(String reviewCode) {
        return reviewRepository.findByReviewCode(reviewCode)
                .orElseThrow(() -> new ProductReviewNotFoundException(REVIEW_NOT_FOUND));
    }
}
