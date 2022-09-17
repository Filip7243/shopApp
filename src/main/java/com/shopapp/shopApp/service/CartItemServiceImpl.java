package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.CartItemNotFoundException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository itemRepository;
    private final ProductRepository productRepository;

    @Override
    public void saveCartItem(CartItem item) {
        itemRepository.save(item);
    }

    @Override
    public CartItem createCartItem(Product product) {
        if(productRepository.existsByProductCode(product.getProductCode())) {
            CartItem item = new CartItem();
            item.setQuantity(0);
            item.setProduct(product);
            return item;
        }

        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public void deleteCartItem(Long id) {
        CartItem foundItem = itemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Item not found!"));
        itemRepository.delete(foundItem);
    }
}
