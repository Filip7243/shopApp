package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;

public interface CartItemService {

    void saveCartItem(CartItem item);
    CartItem createCartItem(Product product);

    void deleteCartItem(Long id);
}
