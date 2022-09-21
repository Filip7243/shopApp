package com.shopapp.shopApp.service.category;

import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.model.Category;

public interface CategoryService {

    void addCategory(Category category);
    void updateCategory(Long id, CategorySaveUpdateDto category);
    void deleteCategoryWithId(Long id);
}
