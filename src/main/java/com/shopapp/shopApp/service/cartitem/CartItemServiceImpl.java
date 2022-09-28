package com.shopapp.shopApp.service.cartitem;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.shopapp.shopApp.constants.ExceptionsConstants.CART_ITEM_NOT_FOUND;
import static com.shopapp.shopApp.constants.ExceptionsConstants.PRODUCT_NOT_FOUND;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository itemRepository;
    private final ProductRepository productRepository;

    @Override
    public void saveCartItem(CartItem item) {
        itemRepository.save(item);
    }

    @Override
    public CartItem createCartItem(Product product) {
        if (productRepository.existsByProductCode(product.getProductCode())) {
            CartItem item = new CartItem();
            item.setQuantity(0);
            item.setProduct(product);
            return item;
        }

        throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, product.getName()));
    }

    @Override
    public void deleteCartItem(Long id) {
        CartItem foundItem = itemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException(CART_ITEM_NOT_FOUND));
        itemRepository.delete(foundItem);
    }
}
