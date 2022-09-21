package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.constants.ResponseConstants;
import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.NotEnoughInStockException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static org.springframework.http.HttpStatus.GONE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;
    private final JwtUtils jwtUtils;
    private final AppUserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<?> createShoppingCart() {
        return ResponseEntity.ok(shoppingCartService.createShoppingCart());
    }

    @GetMapping("/items/{shoppingCartCode}")
    public ResponseEntity<List<CartItem>> getItemsFromShoppingCart(@PathVariable String shoppingCartCode) throws ShoppingCartNotFoundException {
        return ResponseEntity.ok(shoppingCartService.getItemsFromShoppingCart(shoppingCartCode));
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUserToShoppingCart(@RequestParam String shoppingCartCode, HttpServletRequest request)
            throws UserCodeNotFoundException, ShoppingCartNotFoundException, IllegalStateException, UserNotFoundException {

        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        AppUser user = (AppUser) userService.loadUserByUsername(username);
        shoppingCartService.addUserToShoppingCart(shoppingCartCode, user.getUserCode());
        return ResponseEntity.ok(String.format(USER_ADDED_TO_SHOPPING_CART, user.getEmail(), shoppingCartCode));
    }

    @PostMapping("/item/add")
    public ResponseEntity<?> addItemToShoppingCart(@RequestParam String shoppingCartCode, @RequestParam String productCode)
            throws ShoppingCartNotFoundException, ProductNotFoundException, NotEnoughInStockException {

        shoppingCartService.addItemToShoppingCart(shoppingCartCode, productCode, 1);
        return ResponseEntity.ok().body(String.format(SHOPPING_CART_ITEM_ADDED, productCode, shoppingCartCode));
    }

    @DeleteMapping("/item/delete")
    public ResponseEntity<?> deleteItemFromShoppingCart(@RequestParam String shoppingCartCode, @RequestParam Long itemId)
            throws ShoppingCartNotFoundException, CartItemNotFoundException {

        shoppingCartService.deleteItemFromShoppingCart(shoppingCartCode, itemId);
        return ResponseEntity.status(GONE).body(String.format(SHOPPING_CART_ITEM_DELETED, itemId));
    }
}
