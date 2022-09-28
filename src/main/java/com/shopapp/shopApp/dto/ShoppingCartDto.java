package com.shopapp.shopApp.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    private List<CartItemDto> items;
    private Double totalPrice;
}
