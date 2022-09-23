package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.constants.ResponseConstants;
import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.exception.category.CategoryExistsException;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.service.category.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static com.shopapp.shopApp.mapper.CategoryMapper.mapToCategory;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategorySaveUpdateDto category) throws CategoryExistsException {
        categoryService.addCategory(mapToCategory(category));
        return ResponseEntity.status(CREATED).body(String.format(CATEGORY_CREATED, category.getCategoryName()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategorySaveUpdateDto category) throws CategoryNotFoundException {
        categoryService.updateCategory(id, category);
        return ResponseEntity.ok(String.format(CATEGORY_UPDATED, category.getCategoryName()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) throws CategoryNotFoundException {
        categoryService.deleteCategoryWithId(id);
        return ResponseEntity.ok(CATEGORY_DELETED);
    }
}
