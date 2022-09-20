package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

    ShoppingCart createShoppingCart();

    void addUserToShoppingCart(String shoppingCartCode, String appUserCode);

    List<CartItem> getItemsFromShoppingCart(String shoppingCartCode);

    void addItemToShoppingCart(String shoppingCartCode, String productCode, Integer quantity);

    void deleteItemFromShoppingCart(String shoppingCartCode, Long itemId);

    ShoppingCart getShoppingCart(String shoppingCartCode);
}
