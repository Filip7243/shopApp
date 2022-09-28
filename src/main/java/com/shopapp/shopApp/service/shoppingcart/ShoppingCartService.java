package com.shopapp.shopApp.service.shoppingcart;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart createShoppingCart();

    void addUserToShoppingCart(String shoppingCartCode, AppUser user);

    List<CartItem> getItemsFromShoppingCart(String shoppingCartCode);

    void addItemToShoppingCart(String shoppingCartCode, String productCode, Integer quantity);

    void deleteItemFromShoppingCart(String shoppingCartCode, Long itemId);

    ShoppingCart getShoppingCart(String shoppingCartCode);
}
