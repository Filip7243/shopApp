package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderDto {

    private List<CartItem> items;
}
