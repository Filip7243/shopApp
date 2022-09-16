package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.CategorySaveUpdateDto;
import com.shopapp.shopApp.exception.product.CategoryExistsException;
import com.shopapp.shopApp.exception.product.CategoryNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.service.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shopapp.shopApp.mapper.CategoryMapper.mapToCategory;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
    public ResponseEntity<?> addCategory(@RequestBody CategorySaveUpdateDto category) {
        try {
            categoryService.addCategory(mapToCategory(category));
            return ResponseEntity.status(CREATED).body("Added category: " + category.getCategoryName());
        } catch (CategoryExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategorySaveUpdateDto category) {
        try {
            categoryService.updateCategory(id, category);
            return ResponseEntity.ok("Updated category");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryWithId(id);
            return ResponseEntity.ok("Deleted category");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }
}
