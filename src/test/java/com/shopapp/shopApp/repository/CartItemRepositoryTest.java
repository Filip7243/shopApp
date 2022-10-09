package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CartItemRepositoryTest {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private ShoppingCartRepository shoppingCartRepo;
    @Autowired
    private AppUserRepository userRepo;

    private ShoppingCart cart;

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

        AppUser user = new AppUser(
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

        this.cart = new ShoppingCart(
                null,
                UUID.randomUUID().toString(),
                user,
                new ArrayList<>(),
                LocalDateTime.now(),
                0.0
        );

        shoppingCartRepo.save(cart);

        CartItem itemOne = new CartItem(null, 1, opel, cart.getId());
        CartItem itemTwo = new CartItem(null, 1, bmw, cart.getId());

        cartItemRepo.save(itemOne);
        cartItemRepo.save(itemTwo);

        cart.getItems().add(itemOne);
        cart.getItems().add(itemTwo);

        shoppingCartRepo.save(cart);

    }

    @Test
    void itShouldDeleteAllCartItemsByCartId() {
        // given
        int size = cart.getItems().size();
        // when
        int expected = cartItemRepo.deleteByCartId(cart.getId()); // returns number of rows affected
        //then
        assertThat(expected).isEqualTo(size);
    }

    @Test
    void itShouldNotDeleteAnyCartItemsByCartId() {
        // given
        List<CartItem> items = cart.getItems();
        // when
        cartItemRepo.deleteByCartId(any());
        //then
        assertThat(items.size()).isNotZero();
    }
}