package com.shopapp.shopApp.AppUserTests.ProductTests;

import com.shopapp.shopApp.model.*;
import com.shopapp.shopApp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class OrderRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private OrderRepository orderRepo;

    private UserOrder order;

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

        ShoppingCart cart = new ShoppingCart(
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

        this.order = UserOrder.builder()
                .id(null)
                .orderCode(UUID.randomUUID().toString())
                .cart(cart)
                .orderedAt(LocalDateTime.now())
                .hasPaid(false)
                .totalPrice(opel.getPrice() + bmw.getPrice())
                .isDelivered(false)
                .build();

        orderRepo.save(order);
    }

    @Test
    void itShouldFindOrderWithCartJoinedByOrderCode() {
        // when
        Optional<UserOrder> foundOrder = orderRepo.findByOrderCode(order.getOrderCode());
        // then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder).isNotEmpty();
        assertThat(foundOrder).isInstanceOf(Optional.class);
        assertThat(foundOrder.get()).isInstanceOf(UserOrder.class);
    }

    @Test
    void itShouldNotFindOrderByOrderCode() {
        // when
        Optional<UserOrder> foundOrder = orderRepo.findByOrderCode(any());
        // then
        assertThat(foundOrder).isNotPresent();
        assertThat(foundOrder).isEmpty();
        assertThat(foundOrder).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldDeleteOrderByOrderCode() {
        // when
        Integer deletedRows = orderRepo.deleteByOrderCode(order.getOrderCode());
        // then
        assertThat(deletedRows).isEqualTo(1).isNotZero();
        assertThat(deletedRows).isInstanceOf(Integer.class);
    }

    @Test
    void itShouldNotDeleteOrderByOrderCode() {
        // when
        Integer deletedRows = orderRepo.deleteByOrderCode(any());
        // then
        assertThat(deletedRows).isZero();
        assertThat(deletedRows).isInstanceOf(Integer.class);
    }

    @Test
    void itShouldFindAllOrders() {
        // when
        List<UserOrder> allOrders = orderRepo.findAll();
        // then
        assertThat(allOrders).isInstanceOf(List.class);
        assertThat(allOrders.size()).isGreaterThan(0);
    }

    @Test
    void itShouldNotFindAllOrders() {
        // given
        orderRepo.delete(order);
        // when
        List<UserOrder> allOrders = orderRepo.findAll();
        // then
        assertThat(allOrders).isInstanceOf(List.class);
        assertThat(allOrders.size()).isZero();
    }

    @Test
    void itShouldFindOrdersIfDelivered() {
        // given
        order.setIsDelivered(true);
        orderRepo.save(order);
        // when
        List<UserOrder> deliveredOrders = orderRepo.getDeliveredOrders();
        // then
        assertThat(deliveredOrders).isInstanceOf(List.class);
        assertThat(deliveredOrders.size()).isGreaterThan(0);
    }

    @Test
    void itShouldNotFindOrdersIfDelivered() {
        // when
        List<UserOrder> deliveredOrders = orderRepo.getDeliveredOrders();
        // then
        assertThat(deliveredOrders).isInstanceOf(List.class);
        assertThat(deliveredOrders.size()).isZero();
    }
}