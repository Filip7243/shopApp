package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.ProductSaveUpdateDto;
import com.shopapp.shopApp.model.Product;

import java.util.UUID;

public class ProductMapper {

    public static Product mapToProduct(ProductSaveUpdateDto product) {
        return Product.builder()
                .productCode(UUID.randomUUID().toString())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .inStock(product.getInStock())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
