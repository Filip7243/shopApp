package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.service.cartitem.CartItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.CART_ITEM_NOT_FOUND;
import static com.shopapp.shopApp.constants.ExceptionsConstants.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @Mock
    private CartItemRepository itemRepository;
    @Mock
    private ProductRepository productRepository;

    private CartItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        this.itemService = new CartItemServiceImpl(itemRepository, productRepository);
    }

    @Test
    void canSaveCartItem() {
        CartItem item = any();

        itemService.saveCartItem(item);
        verify(itemRepository).save(item);
    }

    @Test
    void canCreateCartItem() {
        var product = new Product();
        var productCode = anyString();
        product.setProductCode(productCode);
        when(productRepository.existsByProductCode(productCode)).thenReturn(true);
        CartItem cartItem = itemService.createCartItem(product);

        assertThat(cartItem).isNotNull();
        assertThat(cartItem).isInstanceOf(CartItem.class);
        assertEquals(product, cartItem.getProduct());
    }

    @Test
    void throwsProductNotFoundExceptionWhenCreateCartItem() {
        var product = new Product();
        var productCode = anyString();
        product.setProductCode(productCode);

        when(productRepository.existsByProductCode(productCode)).thenReturn(false);
        var exception =
                assertThrows(ProductNotFoundException.class, () -> itemService.createCartItem(product));
        assertEquals(String.format(PRODUCT_NOT_FOUND, product.getName()), exception.getMessage());
    }

    @Test
    void canDeleteItem() {
        var item = new CartItem();

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        itemService.deleteCartItem(item.getId());

        verify(itemRepository).delete(item);
    }

    @Test
    void throwsCartItemNotFoundExceptionWhenDeleteItem() {
        Long id = any();
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        var exception =
                assertThrows(CartItemNotFoundException.class, () -> itemService.deleteCartItem(id));
        assertEquals(CART_ITEM_NOT_FOUND, exception.getMessage());
    }
}
