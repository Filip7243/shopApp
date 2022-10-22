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
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
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
    void itShouldFindShoppingCartJoinedWithUserByShoppingCartCode() {
        // when
        Optional<ShoppingCart> foundCart = shoppingCartRepo.findByShoppingCartCode(cart.getShoppingCartCode());
        // then
        assertThat(foundCart).isPresent();
        assertThat(foundCart).isNotEmpty();
        assertThat(foundCart).isInstanceOf(Optional.class);
        assertThat(foundCart.get()).isInstanceOf(ShoppingCart.class);
    }

    @Test
    void itShouldNotFindShoppingCartJoinedWithUserByShoppingCartCode() {
        // when
        Optional<ShoppingCart> foundCart = shoppingCartRepo.findByShoppingCartCode(any());
        // then
        assertThat(foundCart).isNotPresent();
        assertThat(foundCart).isEmpty();
        assertThat(foundCart).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldFindAllShoppingCartsJoinedWithUser() {
        // when
        List<ShoppingCart> allCarts = shoppingCartRepo.findAll();
        // then
        assertThat(allCarts.size()).isNotZero();
        assertThat(allCarts).isInstanceOf(List.class);
    }

    @Test
    void itShouldNotFindAnyShoppingCartBecauseJoinedUserIsNull() {
        // given
        cart.setUser(null);
        shoppingCartRepo.save(cart);
        // when
        List<ShoppingCart> allCarts = shoppingCartRepo.findAll();
        // then
        assertThat(allCarts.size()).isZero();
    }

    @Test
    void itShouldFindShoppingCartJoinedWithUserByShoppingCartId() {
        // when
        Optional<ShoppingCart> foundCart = shoppingCartRepo.findById(cart.getId());
        // then
        assertThat(foundCart).isPresent();
        assertThat(foundCart).isNotEmpty();
        assertThat(foundCart).isInstanceOf(Optional.class);
        assertThat(foundCart.get()).isInstanceOf(ShoppingCart.class);
    }

    @Test
    void itShouldNotFindShoppingCartJoinedWithUserByShoppingCartId() {
        // when
        Optional<ShoppingCart> foundCart = shoppingCartRepo.findById(any());
        // then
        assertThat(foundCart).isNotPresent();
        assertThat(foundCart).isEmpty();
        assertThat(foundCart).isInstanceOf(Optional.class);
    }
}
