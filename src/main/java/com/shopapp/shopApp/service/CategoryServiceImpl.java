package com.shopapp.shopApp.service;

import com.shopapp.shopApp.constants.ExceptionsConstants;
import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.exception.product.CategoryExistsException;
import com.shopapp.shopApp.exception.product.CategoryNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shopapp.shopApp.constants.ExceptionsConstants.CATEGORY_ALREADY_EXISTS;
import static com.shopapp.shopApp.constants.ExceptionsConstants.CATEGORY_NOT_FOUND;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        String name = category.getCategoryName();
        if(categoryRepository.existsByCategoryName(name)) {
            throw new CategoryExistsException(String.format(CATEGORY_ALREADY_EXISTS, category.getCategoryName()));
        }
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, CategorySaveUpdateDto category) {
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(String.format(CATEGORY_ALREADY_EXISTS, category.getCategoryName())));
        foundCategory.setCategoryName(category.getCategoryName());
        foundCategory.setDescription(category.getDescription());
        foundCategory.setImageUrl(category.getImageUrl());
        categoryRepository.save(foundCategory);
    }

    @Override
    public void deleteCategoryWithId(Long id) {
        if(categoryRepository.existsById(id)) { //TODO: change this like it is in update and create private method
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND, "with id: " + id));
        }
    }
}
