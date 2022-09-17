package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.NotEnoughInStockException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService{

    private final ShoppingCartRepository cartRepository;
    private final CartItemServiceImpl itemService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository userRepository;

    @Override
    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setShoppingCartCode(UUID.randomUUID().toString());
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }

    @Override
    public void addUserToShoppingCart(String shoppingCartCode, String appUserCode) {
        AppUser user = userRepository.findByUserCode(appUserCode)
                .orElseThrow(() -> new UserCodeNotFoundException("User NO FOUND"));
        ShoppingCart shoppingCart = cartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Can't find shopping cart"));

        if(user.getShoppingCart() != null) {
            throw new IllegalStateException("User already have shoppingCart");
        }
        shoppingCart.setUser(user);
        cartRepository.save(shoppingCart);

    }

    @Override
    public List<CartItem> getItemsFromShoppingCart(String shoppingCartCode) {
        ShoppingCart shoppingCart = cartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Can't find shopping cart"));//TODO: const for exceptions
        return shoppingCart.getItems();
    }

    @Override
    public void addItemToShoppingCart(String shoppingCartCode, String productCode, Integer quantity) {
        ShoppingCart shoppingCart = cartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Can't find shopping cart"));
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        CartItem item = itemService.createCartItem(product);

        if(quantity > item.getProduct().getInStock()) {
            throw new NotEnoughInStockException("Not Enough in Stock!");
        }

        List<CartItem> items = shoppingCart.getItems();
        for(CartItem cartItem : items) {
            if(cartItem.getProduct().getProductCode().equals(item.getProduct().getProductCode())) {
                item.getProduct().setInStock(item.getProduct().getInStock() - quantity);
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + item.getProduct().getPrice());
                cartRepository.save(shoppingCart);
                return;
            }
        }

        item.getProduct().setInStock(item.getProduct().getInStock() - quantity);
        item.setQuantity(quantity);
        items.add(item);
        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + item.getProduct().getPrice());

        itemService.saveCartItem(item);
        cartRepository.save(shoppingCart);
    }

    @Override
    public void deleteItemFromShoppingCart(String shoppingCartCode, Long itemId) {
        ShoppingCart shoppingCart = cartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Can't find shopping cart"));
        shoppingCart.getItems().remove(cartItemRepository.findById(itemId).orElseThrow()); //TODO: better handle it
        itemService.deleteCartItem(itemId);
        cartRepository.save(shoppingCart);
    }
}
