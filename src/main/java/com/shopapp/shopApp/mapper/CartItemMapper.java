package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.CartItemDto;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.Product;

import java.util.List;

public class CartItemMapper {

    public static CartItemDto mapToDto(CartItem item) {
        Product product = item.getProduct();
        return new CartItemDto(
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                item.getQuantity()
        );
    }

    public static List<CartItemDto> mapToDtoList(List<CartItem> items) {
        return items.stream()
                .map(CartItemMapper::mapToDto)
                .toList();
    }
}
