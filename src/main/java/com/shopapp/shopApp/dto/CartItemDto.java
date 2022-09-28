package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private String name;
    private String desc;
    private String imageUrl;
    private Integer quantity;
//    private List<String> categoryName;

}
