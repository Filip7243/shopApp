package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.exception.product.ProductReviewNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ProductReview;
import com.shopapp.shopApp.repository.ProductReviewRepository;
import com.shopapp.shopApp.service.product.ProductReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.REVIEW_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductReviewServiceTest {

    @Mock
    private ProductReviewRepository productReviewRepo;

    private ProductReviewServiceImpl productReviewService;
    private AppUser user;
    private ProductReview productReview;

    @BeforeEach
    void setUp() {
        this.productReviewService = new ProductReviewServiceImpl(productReviewRepo);
        this.user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                "email",
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                true
        );

        this.productReview = new ProductReview(null, "test", "test", "test", 1, 1L, user);
    }

    @Test
    void canGetUserReviews() {
        List<ProductReviewAddUpdateDto> userReviews = productReviewService.getUserReviews(user);
        verify(productReviewRepo).getProductReviewByUserOrderByStarsDesc(user);
        assertThat(userReviews).isNotNull();
    }

    @Test
    void canAddReview() {
        productReviewService.addReview(productReview);
        verify(productReviewRepo).save(productReview);
    }

    @Test
    void canUpdateReview() {
        when(productReviewRepo.findByReviewCode(productReview.getReviewCode())).thenReturn(Optional.of(productReview));
        productReviewService.updateReview(productReview.getReviewCode(), new ProductReviewAddUpdateDto("test", "test", 1));
        verify(productReviewRepo).save(productReview);
    }

    @Test
    void throwsProductReviewNotFoundExceptionWhenUpdateReview() {
        String reviewCode = anyString();

        when(productReviewRepo.findByReviewCode(reviewCode)).thenReturn(Optional.empty());
        var exception = assertThrows(ProductReviewNotFoundException.class,
                () -> productReviewService.updateReview(reviewCode, new ProductReviewAddUpdateDto("test", "test", 1)));
        assertEquals(REVIEW_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canDeleteReview() {
        when(productReviewRepo.findByReviewCode(productReview.getReviewCode())).thenReturn(Optional.of(productReview));
        productReviewService.deleteReview(productReview.getReviewCode());
        verify(productReviewRepo).delete(productReview);
    }

    @Test
    void throwsProductReviewNotFoundExceptionWhenDeleteReview() {
        String reviewCode = anyString();

        when(productReviewRepo.findByReviewCode(reviewCode)).thenReturn(Optional.empty());
        var exception = assertThrows(ProductReviewNotFoundException.class,
                () -> productReviewService.updateReview(reviewCode, new ProductReviewAddUpdateDto("test", "test", 1)));
        assertEquals(REVIEW_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canFindByUser() {
        productReviewService.findByUser(user);
        verify(productReviewRepo).findByUser(user);
    }
}
