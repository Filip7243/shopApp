package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.model.Category;
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
    private String categoryName;

}
