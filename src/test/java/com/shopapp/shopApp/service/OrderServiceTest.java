package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.OrderRepository;
import com.shopapp.shopApp.repository.ShoppingCartRepository;
import com.shopapp.shopApp.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.ORDER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepo;
    @Mock
    private ShoppingCartRepository shoppingCartRepo;
    @Mock
    private CartItemRepository itemRepo;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        this.orderService = new OrderServiceImpl(orderRepo, shoppingCartRepo, itemRepo);
    }

    @Test
    void canCreateOrder() {
        ShoppingCart cart = new ShoppingCart();

        UserOrder order = orderService.createOrder(cart);
        verify(orderRepo).save(order);
    }

    @Test
    void canDeleteOrder() {
        var code = anyString();

        when(orderRepo.deleteByOrderCode(code)).thenReturn(1);
        orderService.deleteOrder(code);
    }

    @Test
    void throwsOrderNotFoundExceptionWhenDeleteOrder() {
        var code = anyString();

        when(orderRepo.deleteByOrderCode(code)).thenReturn(0);
        var exception =
                assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(code));

        assertEquals(String.format(ORDER_NOT_FOUND, code), exception.getMessage());
    }

    @Test
    void canGetUserOrder() {
        var code = anyString();
        var order = new UserOrder();
        order.setOrderCode(code);

        when(orderRepo.findByOrderCode(code)).thenReturn(Optional.of(order));
        UserOrder foundOrder = orderService.getOrder(code);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder).isInstanceOf(UserOrder.class);
        assertThat(foundOrder.getOrderCode()).isEqualTo(code);
    }

    @Test
    void throwsOrderNotFoundExceptionWhenGetUserOrder() {
        var code = anyString();

        when(orderRepo.findByOrderCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(code));

        assertEquals(String.format(ORDER_NOT_FOUND, code), exception.getMessage());
    }

    @Test
    void canCompleteOrder() {
        var code = anyString();

        var cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());

        var order = new UserOrder();
        order.setCart(cart);
        order.setOrderCode(code);

        when(orderRepo.findByOrderCode(code)).thenReturn(Optional.of(order));
        orderService.completeOrder(code);

        assertThat(order).isNotNull();
        assertThat(order).isInstanceOf(UserOrder.class);
        assertThat(order.getOrderCode()).isEqualTo(code);
        assertTrue(order.getHasPaid());
        assertThat(order.getCart().getItems().size()).isEqualTo(0).isZero();
        assertThat(order.getCart().getTotalPrice()).isEqualTo(0.0).isZero();

        verify(orderRepo).save(order);
        verify(shoppingCartRepo).save(cart);
    }

    @Test
    void throwsOrderNotFoundExceptionWhenCompleteOrder() {
        var code = anyString();

        when(orderRepo.findByOrderCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(OrderNotFoundException.class, () -> orderService.completeOrder(code));

        assertEquals(String.format(ORDER_NOT_FOUND, code), exception.getMessage());
    }

}
