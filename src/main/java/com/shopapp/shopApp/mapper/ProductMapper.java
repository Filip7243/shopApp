package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.dto.ProductSaveUpdateDto;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Objects;
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
                .categoryName(getCategoriesNames(product.getCategories()))
                .build();
    }

    public static List<String> getCategoriesNames(List<Category> categories) {
        return categories.stream().map(Category::getCategoryName).toList();
    }

    public static Set<ProductDisplayDto> getSetOfProductsDto(Set<Product> products) {
        return products.stream()
                .map(ProductMapper::mapToProductDisplayDto)
                .collect(Collectors.toSet());
    }
}
