package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.NotEnoughInStockException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.repository.ShoppingCartRepository;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository cartRepo;
    @Mock
    private ProductRepository productRepo;
    @Mock
    private CartItemRepository cartItemRepo;

    private ShoppingCartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        this.cartService = new ShoppingCartServiceImpl(cartRepo, productRepo, cartItemRepo);
    }

    @Test
    void canCreateShoppingCart() {
        ShoppingCart shoppingCart = cartService.createShoppingCart();

        verify(cartRepo).save(shoppingCart);
        assertThat(shoppingCart).isNotNull();
        assertThat(shoppingCart).isInstanceOf(ShoppingCart.class);
    }

    @Test
    void canAddUserToShoppingCart() {
        var cart = new ShoppingCart();
        var shoppingCartCode = anyString();
        cart.setShoppingCartCode(shoppingCartCode);

        when(cartRepo.findByShoppingCartCode(shoppingCartCode)).thenReturn(Optional.of(cart));
        cartService.addUserToShoppingCart(shoppingCartCode, new AppUser());

        verify(cartRepo).save(cart);
    }

    @Test
    void throwsShoppingCartNotFoundExceptionWhenAddUserToSHoppingCart() {
        var shoppingCartCode = anyString();

        when(cartRepo.findByShoppingCartCode(shoppingCartCode)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ShoppingCartNotFoundException.class, () -> cartService.addUserToShoppingCart(shoppingCartCode, new AppUser()));

        assertEquals(SHOPPING_CART_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canAddItemToShoppingCart() {
        var code = anyString();

        var product = new Product();
        product.setProductCode(code);
        product.setInStock(Integer.MAX_VALUE);
        product.setPrice(100.00);

        var cart = new ShoppingCart();
        cart.setShoppingCartCode(code);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(cart));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.of(product));
        cartService.addItemToShoppingCart(cart.getShoppingCartCode(), product.getProductCode(), 111);

        verify(cartRepo).save(cart);
    }

    @Test
    void canAddItemToShoppingCartWhenItemAlreadyExistsInCart() {
        var code = anyString();

        var product = new Product();
        product.setProductCode(code);
        product.setInStock(Integer.MAX_VALUE);
        product.setPrice(100.00);

        var cart = new ShoppingCart();
        cart.setShoppingCartCode(code);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        var item = new CartItem();
        item.setProduct(product);
        item.setCartId(cart.getId());
        item.setQuantity(0);

        cart.getItems().add(item);
        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(cart));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.of(product));
        cartService.addItemToShoppingCart(cart.getShoppingCartCode(), product.getProductCode(), 111);

        verify(cartRepo).save(cart);
    }

    @Test
    void throwsNotEnoughInStockExceptionWhenAddItemToShoppingCart() {
        var code = anyString();

        var product = new Product();
        product.setProductCode(code);
        product.setInStock(Integer.MIN_VALUE);
        product.setPrice(100.00);

        var cart = new ShoppingCart();
        cart.setShoppingCartCode(code);
        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0.0);

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(cart));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.of(product));

        var exception =
                assertThrows(NotEnoughInStockException.class, () -> cartService.addItemToShoppingCart(cart.getShoppingCartCode(), product.getProductCode(), 111));

        assertEquals(NOT_ENOUGH_IN_STOCK, exception.getMessage());
    }

    @Test
    void throwsShoppingCartNotFoundExceptionWhenAddItemToShoppingCart() {
        var code = anyString();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ShoppingCartNotFoundException.class, () -> cartService.addItemToShoppingCart(code, code, 1));
        assertEquals(SHOPPING_CART_NOT_FOUND, exception.getMessage());
    }

    @Test
    void throwsProductNotFoundExceptionWhenAddItemToShoppingCart() {
        var cart = new ShoppingCart();
        var code = anyString();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(cart));
        when(productRepo.findByProductCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ProductNotFoundException.class, () -> cartService.addItemToShoppingCart(code, code, 1));
        assertEquals(String.format(PRODUCT_NOT_FOUND, "with code: " + code), exception.getMessage());
    }

    @Test
    void canDeleteItemFromShoppingCart() {
        var cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());
        var code = anyString();
        var item = new CartItem();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        cartService.deleteItemFromShoppingCart(code, item.getId());

        verify(cartItemRepo).delete(item);
        verify(cartRepo).save(cart);
    }

    @Test
    void throwsShoppingCartNotFoundExceptionWhenDeleteItemFromShoppingCart() {
        var code = anyString();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ShoppingCartNotFoundException.class, () -> cartService.deleteItemFromShoppingCart(code, 1L));
        assertEquals(SHOPPING_CART_NOT_FOUND, exception.getMessage());
    }

    @Test
    void throwsCartItemNotFoundExceptionWhenDeleteItemFromShoppingCart() {
        var code = anyString();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(new ShoppingCart()));
        when(cartItemRepo.findById(any())).thenReturn(Optional.empty());
        var exception =
                assertThrows(CartItemNotFoundException.class, () -> cartService.deleteItemFromShoppingCart(code, 1L));
        assertEquals(CART_ITEM_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canGetShoppingCart() {
        var code = anyString();
        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.of(new ShoppingCart()));
        ShoppingCart shoppingCart = cartService.getShoppingCart(code);

        assertThat(shoppingCart).isNotNull();
        assertThat(shoppingCart).isInstanceOf(ShoppingCart.class);
    }

    @Test
    void throwsShoppingCartNotFoundExceptionWhenGetShoppingCart() {
        var code = anyString();

        when(cartRepo.findByShoppingCartCode(code)).thenReturn(Optional.empty());
        var exception =
                assertThrows(ShoppingCartNotFoundException.class, () -> cartService.getShoppingCart(code));
        assertEquals(SHOPPING_CART_NOT_FOUND, exception.getMessage());
    }
}
