package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.model.Category;

public class CategoryMapper {

    public static Category mapToCategory(CategorySaveUpdateDto category) {
        return Category.builder()
                .id(null)
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .build();
    }
}
