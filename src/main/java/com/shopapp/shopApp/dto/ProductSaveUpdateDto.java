package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveUpdateDto {

    private String name;
    private String description;
    private Double price;
    private Integer inStock;
    private String imageUrl;
    // TODO: add Category field for ui
}
