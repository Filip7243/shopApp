package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.ProductSaveUpdateDto;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import com.shopapp.shopApp.exception.product.ProductExistsException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.service.product.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shopapp.shopApp.mapper.ProductMapper.mapToProduct;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductSaveUpdateDto product) {
        try {
            productService.addProduct(mapToProduct(product));
            return ResponseEntity.status(CREATED).body("Product created");
        } catch (ProductExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategoryToProduct(@RequestParam String productCode, @RequestParam String categoryName) {
        try {
            productService.addCategoryToProduct(productCode, categoryName);
            return ResponseEntity.status(CREATED).body("Category: " + categoryName + " added to product");
        } catch (ProductNotFoundException | CategoryNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{productCode}")
    public ResponseEntity<?> updateProduct(@PathVariable String productCode,
                                           @RequestBody ProductSaveUpdateDto product) {
        try {
            productService.updateProduct(productCode, mapToProduct(product));
            return ResponseEntity.ok("Product updated");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productCode}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productCode) {
        try {
            productService.deleteProductWithProductCode(productCode);
            return ResponseEntity.ok("Product deleted");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        }
    }

}
