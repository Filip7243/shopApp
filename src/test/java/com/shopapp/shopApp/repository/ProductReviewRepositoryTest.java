package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.ProductReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ProductReviewRepositoryTest {

    @Autowired
    private ProductReviewRepository productReviewRepo;
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;

    private AppUser user;
    private ProductReview opelReview;
    private ProductReview bmwReview;
    private Product opel;
    private Product bmw;

    @BeforeEach
    void setUp() {
        this.opel = Product.builder()
                .id(null)
                .productCode(UUID.randomUUID().toString())
                .name("Opel")
                .description("This is a car")
                .price(120000.250)
                .inStock(5)
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        this.bmw = Product.builder()
                .id(null)
                .productCode(UUID.randomUUID().toString())
                .name("BMW")
                .description("This is a car")
                .price(420000.123)
                .inStock(10)
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        productRepo.save(opel);
        productRepo.save(bmw);

        this.user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                "test@mail.com",
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

        userRepo.save(user);

        this.opelReview = ProductReview.builder()
                .id(null)
                .reviewCode(UUID.randomUUID().toString())
                .topic("Test OPEL Product Review")
                .description("Test description")
                .stars(5)
                .productId(opel.getId())
                .user(user)
                .build();

        this.bmwReview = ProductReview.builder()
                .id(null)
                .reviewCode(UUID.randomUUID().toString())
                .topic("Test BMW Product Review")
                .description("Test description")
                .stars(3)
                .productId(bmw.getId())
                .user(user)
                .build();

        productReviewRepo.save(opelReview);
        productReviewRepo.save(bmwReview);
    }

    @Test
    void itShouldFindProductReviewByUser() {
        // when
        List<ProductReview> foundUser = productReviewRepo.findByUser(user);
        // then
        assertThat(foundUser.size()).isNotZero().isEqualTo(2);
        assertThat(foundUser).isInstanceOf(List.class);
    }

    @Test
    void itShouldNotFindProductReviewByUserBecauseUserDoesNotExist() {
        // when
        List<ProductReview> foundUser = productReviewRepo.findByUser(any());
        // then
        assertThat(foundUser.size()).isZero();
        assertThat(foundUser).isInstanceOf(List.class);
    }

    @Test
    void itShouldFindProductReviewByProductReviewCode() {
        // when
        Optional<ProductReview> bmwReview = productReviewRepo.findByReviewCode(this.bmwReview.getReviewCode());
        Optional<ProductReview> opelReview = productReviewRepo.findByReviewCode(this.opelReview.getReviewCode());
        // then
        assertThat(bmwReview).isPresent();
        assertThat(bmwReview).isNotEmpty();
        assertThat(bmwReview).isInstanceOf(Optional.class);
        assertThat(bmwReview.get()).isInstanceOf(ProductReview.class);

        assertThat(opelReview).isPresent();
        assertThat(opelReview).isNotEmpty();
        assertThat(opelReview).isInstanceOf(Optional.class);
        assertThat(opelReview.get()).isInstanceOf(ProductReview.class);
    }

    @Test
    void itShouldNotFindProductReviewByProductReviewCode() {
        // when
        Optional<ProductReview> foundReview = productReviewRepo.findByReviewCode(any());
        // then
        assertThat(foundReview).isNotPresent();
        assertThat(foundReview).isEmpty();
        assertThat(foundReview).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldFindProductReviewByUserOrderByStarsDesc() {
        // when
        List<ProductReviewAddUpdateDto> userReviews = productReviewRepo.getProductReviewByUserOrderByStarsDesc(user);
        // then
        assertThat(userReviews.size()).isNotZero().isEqualTo(2);
        assertThat(userReviews).isInstanceOf(List.class);
        assertThat(userReviews.get(0)).isInstanceOf(ProductReviewAddUpdateDto.class);
        assertThat(userReviews.get(0).getTopic().contains("OPEL")).isTrue();
        assertThat(userReviews.get(0).getTopic().contains("BMW")).isFalse();
    }

    @Test
    void itShouldNotFindProductReviewByUserOrderByStarsDescBecauseUserDoesNotExist() {
        // when
        List<ProductReviewAddUpdateDto> userReviews = productReviewRepo.getProductReviewByUserOrderByStarsDesc(any());
        // then
        assertThat(userReviews.size()).isZero();
        assertThat(userReviews).isInstanceOf(List.class);
    }

    @Test
    void itShouldFindProductReviewByProductIdIn() {
        // when
        List<ProductReview> foundReviews = productReviewRepo.findAllByProductIdIn(List.of(bmw.getId(), opel.getId()));
        // then
        assertThat(foundReviews.size()).isNotZero().isEqualTo(2);
        assertThat(foundReviews).isInstanceOf(List.class);
        assertThat(foundReviews.get(0)).isInstanceOf(ProductReview.class);
    }

    @Test
    void itShouldNotFindProductReviewByProductIdIn() {
        // when
        List<ProductReview> foundReviews = productReviewRepo.findAllByProductIdIn(List.of(10L));
        // then
        assertThat(foundReviews.size()).isZero();
        assertThat(foundReviews).isInstanceOf(List.class);
    }
}