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

import javax.validation.Valid;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static com.shopapp.shopApp.mapper.ProductMapper.mapToProduct;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }//TODO: product display dto

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductSaveUpdateDto product) throws ProductExistsException {
        productService.addProduct(mapToProduct(product));
        return ResponseEntity.status(CREATED).body(String.format(PRODUCT_CREATED, product.getName()));
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategoryToProduct(@RequestParam String productCode, @RequestParam String categoryName)
            throws ProductNotFoundException, CategoryNotFoundException {

        productService.addCategoryToProduct(productCode, categoryName);
        return ResponseEntity.status(CREATED).body(String.format(CATEGORY_ADDED_TO_PRODUCT, categoryName, productCode));
    }

    @PutMapping("/update/{productCode}")
    public ResponseEntity<?> updateProduct(@PathVariable String productCode,
                                           @RequestBody @Valid ProductSaveUpdateDto product) throws ProductNotFoundException {

        productService.updateProduct(productCode, mapToProduct(product));
        return ResponseEntity.ok(String.format(PRODUCT_UPDATED, productCode));
    }

    @DeleteMapping("/delete/{productCode}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productCode) throws ProductNotFoundException {
        productService.deleteProductWithProductCode(productCode);
        return ResponseEntity.ok(String.format(PRODUCT_DELETED, productCode));
    }

}
