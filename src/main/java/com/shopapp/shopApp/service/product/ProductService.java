package com.shopapp.shopApp.service.product;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;

import java.util.List;

public interface ProductService {

    void addProduct(Product product);

    void updateProduct(String productCode, Product product);

    void deleteProductWithProductCode(String productCode);

    void addCategoryToProduct(String productCode, String categoryName);

    List<ProductDisplayDto> getAllProducts(int page);
}
