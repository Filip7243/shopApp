package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.product.CategoryNotFoundException;
import com.shopapp.shopApp.exception.product.ProductExistsException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CategoryRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(Product product) {
        String productCode = product.getProductCode();
        if(productRepository.existsByProductCode(productCode)) {
            throw new ProductExistsException("Product with code: " + productCode + " exists");
        }
        productRepository.save(product);
    }

    @Override
    public void updateProduct(String productCode, Product product) {
        Product foundProduct = getProductWithProductCode(productCode);
        foundProduct.setName(product.getName());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setInStock(product.getInStock());
        foundProduct.setImageUrl(product.getImageUrl());

        productRepository.save(foundProduct);
    }

    @Override
    public void deleteProductWithProductCode(String productCode) {
        Product product = getProductWithProductCode(productCode);
        productRepository.delete(product);
    }

    @Override
    public void addCategoryToProduct(String productCode, String categoryName) {
        Product product = getProductWithProductCode(productCode);
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name: " + categoryName + " not found"));
        product.setCategory(category);

        productRepository.save(product);
    }

    private Product getProductWithProductCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product with code: " + productCode + " not found"));
    }
}
