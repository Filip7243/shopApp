package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.model.ShoppingCart;

import java.util.List;

public interface OrderService {

    void createOrder(ShoppingCart shoppingCart);
    void updateOrder(String orderCode, UserOrderDto orderDto);
    void deleteOrder(String orderCode);

    UserOrder getOrder(String shoppingCartCode);
}
