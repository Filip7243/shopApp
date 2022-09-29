package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.model.UserOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderMapper {
    //todo: FORMS FOR UI
    public static UserOrder mapToOrder(ShoppingCart shoppingCart) {
        return UserOrder.builder()
                .id(null)
                .orderCode(UUID.randomUUID().toString())
//                .cartCode(shoppingCart.getShoppingCartCode())
                .cart(shoppingCart)
                .orderedItems(getCartItems(shoppingCart))
                .orderedAt(LocalDateTime.now())
                .hasPaid(false)
                .totalPrice(shoppingCart.getTotalPrice())
                .build();
    }
//todo; wishlist
    private static List<CartItem> getCartItems(ShoppingCart shoppingCart) {
        return shoppingCart.getItems().stream().toList();
    }
}
