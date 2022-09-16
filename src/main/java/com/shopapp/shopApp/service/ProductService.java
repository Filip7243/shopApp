package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;

public interface ProductService {

    void addProduct(Product product);

    void updateProduct(String productCode, Product product);

    void deleteProductWithProductCode(String productCode);

    void addCategoryToProduct(String productCode, String categoryName);
}
