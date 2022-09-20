package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDisplayDto {

    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String categoryName;
}
