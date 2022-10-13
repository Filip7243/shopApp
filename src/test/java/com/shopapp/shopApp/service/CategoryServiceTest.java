package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.exception.category.CategoryExistsException;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.repository.CategoryRepository;
import com.shopapp.shopApp.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.CATEGORY_ALREADY_EXISTS;
import static com.shopapp.shopApp.constants.ExceptionsConstants.CATEGORY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepo;

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        this.categoryService = new CategoryServiceImpl(categoryRepo);
    }

    @Test
    void canGetAllCategories() {
        categoryService.getCategories();
        verify(categoryRepo).findAll();
    }

    @Test
    void canAddCategory() {
        var categoryName = anyString();
        var category = new Category(null, categoryName, "test", "test");

        when(categoryRepo.existsByCategoryName(categoryName)).thenReturn(false);
        categoryService.addCategory(category);
        verify(categoryRepo).save(category);
    }

    @Test
    void throwsCategoryExistsExceptionWhenAddCategory() {
        var categoryName = anyString();
        var category = new Category(null, categoryName, "test", "test");

        when(categoryRepo.existsByCategoryName(categoryName)).thenReturn(true);
        CategoryExistsException exception =
                assertThrows(CategoryExistsException.class, () -> categoryService.addCategory(category));
        assertEquals(String.format(CATEGORY_ALREADY_EXISTS, category.getCategoryName()), exception.getMessage());
    }

    @Test
    void canUpdateCategory() {
        var category = new Category(null, "test", "test", "test");

        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        categoryService.updateCategory(category.getId(), new CategorySaveUpdateDto());
        verify(categoryRepo).save(category);
    }

    @Test
    void throwsCategoryNotFoundExceptionWhenUpdateCategory() {
        var category = new Category(null, "test", "test", "test");

        when(categoryRepo.findById(category.getId())).thenReturn(Optional.empty());
        CategoryNotFoundException exception =
                assertThrows(CategoryNotFoundException.class,
                        () -> categoryService.updateCategory(category.getId(),
                                new CategorySaveUpdateDto(category.getCategoryName(), category.getDescription(), category.getImageUrl())));
        assertEquals(String.format(CATEGORY_ALREADY_EXISTS, category.getCategoryName()), exception.getMessage());
    }

    @Test
    void canDeleteCategoryWithId() {
        var category = new Category(null, "test", "test", "test");

        when(categoryRepo.existsById(category.getId())).thenReturn(true);
        categoryService.deleteCategoryWithId(category.getId());
        verify(categoryRepo).deleteById(category.getId());
    }

    @Test
    void throwsCategoryNotFoundExceptionWhenDeleteCategoryWithId() {
        var category = new Category(null, "test", "test", "test");

        when(categoryRepo.existsById(category.getId())).thenReturn(false);
        CategoryNotFoundException exception =
                assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryWithId(category.getId()));
        assertEquals(String.format(CATEGORY_NOT_FOUND, "with id: " + category.getId()), exception.getMessage());
    }
}
