package com.shopapp.shopApp.service;

import com.shopapp.shopApp.constants.ExceptionsConstants;
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

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;

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
            throw new ProductExistsException(String.format(PRODUCT_ALREADY_EXISTS, product.getName()));
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
                .orElseThrow(() -> new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND, categoryName)));
        product.setCategory(category);

        productRepository.save(product);
    }

    public Product getProductWithProductCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, "with code: " + productCode)));
    }
}
