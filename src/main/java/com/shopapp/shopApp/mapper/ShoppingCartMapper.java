package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.ShoppingCartDto;
import com.shopapp.shopApp.model.ShoppingCart;

public class ShoppingCartMapper {

    public static ShoppingCartDto mapToShoppingCartDto(ShoppingCart cart) {
        return ShoppingCartDto.builder()
                .items(CartItemMapper.mapToDtoList(cart.getItems()))
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
