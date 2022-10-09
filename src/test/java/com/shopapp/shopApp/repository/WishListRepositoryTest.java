package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class WishListRepositoryTest {

    //tood; check if injections are not null
    @Autowired
    private WishListRepository wishListRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private AppUserRepository userRepo;

    private AppUser user;
    private WishList wishList;

    @BeforeEach
    void setUp() {
        Product opel = Product.builder()
                .id(null)
                .productCode(UUID.randomUUID().toString())
                .name("Opel")
                .description("This is a car")
                .price(120000.250)
                .inStock(5)
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        Product bmw = Product.builder()
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

        this.wishList = new WishList(null, UUID.randomUUID().toString(), new HashSet<>(), user);
        wishListRepo.save(wishList);

        wishList.getWishListItems().add(opel);
        wishList.getWishListItems().add(bmw);
    }

    @Test
    void itShouldFindWishListByUser() {
        // when
        Optional<WishList> foundWishList = wishListRepo.findByUser(user);
        //then
        assertThat(foundWishList).isPresent();
        assertThat(foundWishList).isNotEmpty();
        assertThat(foundWishList).isInstanceOf(Optional.class);
        assertThat(foundWishList.get()).isInstanceOf(WishList.class);
    }

    @Test
    void itShouldNotFindWishListByUser() {
        // when
        Optional<WishList> foundWishList = wishListRepo.findByUser(any());
        //then
        assertThat(foundWishList).isNotPresent();
        assertThat(foundWishList).isEmpty();
        assertThat(foundWishList).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldFindWishListJoinedWithProductListByWishListCode() {
        // when
        Optional<WishList> foundWishList = wishListRepo.findByWishListCode(wishList.getWishListCode());
        //then
        assertThat(foundWishList).isPresent();
        assertThat(foundWishList).isNotEmpty();
        assertThat(foundWishList).isInstanceOf(Optional.class);
        assertThat(foundWishList.get()).isInstanceOf(WishList.class);
    }

    @Test
    void itShouldNotFindWishListJoinedWithProductsByWishListCodeBecauseProductListIsEmpty() {
        // given
        wishList.getWishListItems().clear();
        wishListRepo.save(wishList);
        // when
        Optional<WishList> foundWishList = wishListRepo.findByWishListCode(wishList.getWishListCode());
        //then
        assertThat(foundWishList).isNotPresent();
        assertThat(foundWishList).isEmpty();
        assertThat(foundWishList).isInstanceOf(Optional.class);
    }

    @Test
    void itChecksIfWishListExistsWithWishListCode() {
        // when
        Boolean expected = wishListRepo.existsByWishListCode(wishList.getWishListCode());
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itChecksIfWishListDoesNotExistsWithWishListCode() {
        // when
        Boolean expected = wishListRepo.existsByWishListCode(anyString());
        //then
        assertThat(expected).isFalse();
    }
}
