package com.shopapp.shopApp.service.shoppingcart;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
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
import com.shopapp.shopApp.service.cartitem.CartItemServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;

@Service
@Transactional
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    //    private final CartItemServiceImpl itemService;

    @Override
    public ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setShoppingCartCode(UUID.randomUUID().toString());
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public void addUserToShoppingCart(String shoppingCartCode, AppUser user) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartCode);

        shoppingCart.setUser(user);
        cartRepository.save(shoppingCart);
    }

    @Override
    public void addItemToShoppingCart(String shoppingCartCode, String productCode, Integer quantity) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartCode);
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, "with code: " + productCode)));

//        CartItem item = itemService.createCartItem(product);
        CartItem item = new CartItem();
        item.setQuantity(0);
        item.setProduct(product);
        item.setCartId(shoppingCart.getId());

        if (quantity > item.getProduct().getInStock()) {
            throw new NotEnoughInStockException(NOT_ENOUGH_IN_STOCK);
        }

        List<CartItem> items = shoppingCart.getItems();
        for (CartItem cartItem : items) {
            if (cartItem.getProduct().getProductCode().equals(item.getProduct().getProductCode())) {
                //        item.getProduct().setInStock(item.getProduct().getInStock() - quantity);
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

        cartItemRepository.save(item);
        cartRepository.save(shoppingCart);
    }

    @Override
    public void deleteItemFromShoppingCart(String shoppingCartCode, Long itemId) {
        ShoppingCart shoppingCart = getShoppingCart(shoppingCartCode);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(CART_ITEM_NOT_FOUND));
        shoppingCart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getShoppingCart(String shoppingCartCode) {
        return cartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND));
    }

    //todo; test all endpoints
}
