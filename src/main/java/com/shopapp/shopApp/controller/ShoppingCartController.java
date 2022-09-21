package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.NotEnoughInStockException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;

    @PostMapping("/create")
    public ResponseEntity<?> createShoppingCart() {
        return ResponseEntity.ok(shoppingCartService.createShoppingCart());
    }

    @GetMapping("/items/{shoppingCartCode}")
    public ResponseEntity<List<CartItem>> getItemsFromShoppingCart(@PathVariable String shoppingCartCode) throws ShoppingCartNotFoundException {
        return ResponseEntity.ok(shoppingCartService.getItemsFromShoppingCart(shoppingCartCode));
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUserToShoppingCart(@RequestParam String shoppingCartCode, @RequestParam String appUserCode)
            throws UserCodeNotFoundException, ShoppingCartNotFoundException, IllegalStateException {

        shoppingCartService.addUserToShoppingCart(shoppingCartCode, appUserCode);
        return ResponseEntity.ok("ADDED ITEM");
    }

    @PostMapping("/item/add")
    public ResponseEntity<?> addItemToShoppingCart(@RequestParam String shoppingCartCode, @RequestParam String productCode)
            throws ShoppingCartNotFoundException, ProductNotFoundException, NotEnoughInStockException {

        shoppingCartService.addItemToShoppingCart(shoppingCartCode, productCode, 1);
        return ResponseEntity.ok().body("Item has been added");
    }

    @DeleteMapping("/item/delete")
    public ResponseEntity<?> deleteItemFromShoppingCart(@RequestParam String shoppingCartCode, @RequestParam Long itemId)
            throws ShoppingCartNotFoundException, CartItemNotFoundException {

        shoppingCartService.deleteItemFromShoppingCart(shoppingCartCode, itemId);
        return ResponseEntity.ok().body("DELETED");
    }
}
