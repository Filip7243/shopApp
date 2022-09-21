package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.dto.ProductSaveUpdateDto;
import com.shopapp.shopApp.model.Product;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static ProductDisplayDto mapToProductDisplayDto(Product product) {
        return ProductDisplayDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }

    public static Set<ProductDisplayDto> getSetOfProductsDto(Set<Product> products) {
        return products.stream()
                .map(ProductMapper::mapToProductDisplayDto)
                .collect(Collectors.toSet());
    }
}
