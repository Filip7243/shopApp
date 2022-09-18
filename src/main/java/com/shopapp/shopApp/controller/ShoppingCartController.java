package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.NotEnoughInStockException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.service.ShoppingCartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<List<CartItem>> getItemsFromShoppingCart(@PathVariable String shoppingCartCode) {
        try {
            return ResponseEntity.ok(shoppingCartService.getItemsFromShoppingCart(shoppingCartCode));
        } catch (ShoppingCartNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUserToShoppingCart(@RequestParam String shoppingCartCode, @RequestParam String appUserCode) {
        try {
            shoppingCartService.addUserToShoppingCart(shoppingCartCode, appUserCode);
            return ResponseEntity.ok("ADDED ITEM");
        } catch (UserCodeNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/item/add")
    public ResponseEntity<?> addItemToShoppingCart(@RequestParam String shoppingCartCode, @RequestParam String productCode) {
        try {
            shoppingCartService.addItemToShoppingCart(shoppingCartCode, productCode, 1);
            return ResponseEntity.ok().body("Added item");
        } catch (ShoppingCartNotFoundException |
                 ProductNotFoundException |
                 NotEnoughInStockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/item/delete")
    public ResponseEntity<?> deleteItemFromShoppingCart(@RequestParam String shoppingCartCode, @RequestParam Long itemId) {
        try {
            shoppingCartService.deleteItemFromShoppingCart(shoppingCartCode, itemId);
            return ResponseEntity.ok().body("DELETED");
        } catch (ShoppingCartNotFoundException | CartItemNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
