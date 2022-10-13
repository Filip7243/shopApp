package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CategoryRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.repository.ProductReviewRepository;
import com.shopapp.shopApp.service.product.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.CATEGORY_NOT_FOUND;
import static com.shopapp.shopApp.constants.ExceptionsConstants.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;
    @Mock
    private CategoryRepository categoryRepo;
    @Mock
    private ProductReviewRepository productReviewRepo;

    private ProductServiceImpl productService;
    private Product p0, p1, p2, p3, p4;

    @BeforeEach
    void setUp() {
        this.productService = new ProductServiceImpl(productRepo, categoryRepo, productReviewRepo);

        this.p0 = Product.builder()
                .id(1L)
                .productCode(UUID.randomUUID().toString())
                .name("p1")
                .description("p1desc")
                .price(22.90)
                .inStock(10)
                .imageUrl("testImg")
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        this.p1 = Product.builder()
                .id(2L)
                .productCode(UUID.randomUUID().toString())
                .name("p2")
                .description("p2desc")
                .price(22.90)
                .inStock(10)
                .imageUrl("testImg")
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        this.p2 = Product.builder()
                .id(3L)
                .productCode(UUID.randomUUID().toString())
                .name("p3")
                .description("p3desc")
                .price(22.90)
                .inStock(10)
                .imageUrl("testImg")
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        this.p3 = Product.builder()
                .id(4L)
                .productCode(UUID.randomUUID().toString())
                .name("p4")
                .description("p4desc")
                .price(22.90)
                .inStock(10)
                .imageUrl("testImg")
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        this.p4 = Product.builder()
                .id(5L)
                .productCode(UUID.randomUUID().toString())
                .name("p5")
                .description("p5desc")
                .price(22.90)
                .inStock(10)
                .imageUrl("testImg")
                .categories(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
    }

    @Test
    void canGetFirstFiveProducts() {
        var page = 0;
        var size = 5;
        List<Product> allProducts = List.of(p0, p1, p2, p3, p4);

        when(productRepo.findAllProducts(PageRequest.of(page, size))).thenReturn(allProducts);
        var ids = allProducts.stream().map(Product::getId).toList();
        when(productReviewRepo.findAllByProductIdIn(ids)).thenReturn(new ArrayList<>());
        List<ProductDisplayDto> result = productService.getAllProducts(page);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(size);
    }

    @Test
    void canAddProduct() {
        productService.addProduct(p0);
        verify(productRepo).save(p0);
    }

    @Test
    void canUpdateProduct() {
        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.of(p0));
        productService.updateProduct(p0.getProductCode(), new Product());
        verify(productRepo).save(p0);
    }

    @Test
    void throwsProductNotFoundExceptionWhenUpdateProduct() {
        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.empty());
        var exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(p0.getProductCode(), new Product()));
        assertEquals(String.format(PRODUCT_NOT_FOUND, "with code: " + p0.getProductCode()), exception.getMessage());
    }

    @Test
    void canDeleteProductWithProductCode() {
        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.of(p0));
        productService.deleteProductWithProductCode(p0.getProductCode());
        verify(productReviewRepo).deleteAll(p0.getReviews());
        verify(productRepo).delete(p0);
    }

    @Test
    void throwsProductNotFoundExceptionWhenDeleteProductWithProductCode() {
        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.empty());
        var exception = assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProductWithProductCode(p0.getProductCode()));
        assertEquals(String.format(PRODUCT_NOT_FOUND, "with code: " + p0.getProductCode()), exception.getMessage());
    }

    @Test
    void canAddCategoryToProduct() {
        var category = new Category(null, "test", "test", "test");

        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.of(p0));
        when(categoryRepo.findByCategoryName(category.getCategoryName())).thenReturn(Optional.of(category));
        productService.addCategoryToProduct(p0.getProductCode(), category.getCategoryName());
        verify(productRepo).save(p0);
    }

    @Test
    void throwsProductNotFoundExceptionWhenAddCategoryToProduct() {
        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.empty());
        var exception = assertThrows(ProductNotFoundException.class,
                () -> productService.addCategoryToProduct(p0.getProductCode(), "test"));
        assertEquals(String.format(PRODUCT_NOT_FOUND, "with code: " + p0.getProductCode()), exception.getMessage());
    }

    @Test
    void throwsCategoryNotFoundExceptionWhenAddCategoryToProduct() {
        var categoryName = "test";

        when(productRepo.findByProductCode(p0.getProductCode())).thenReturn(Optional.of(p0));
        when(categoryRepo.findByCategoryName(categoryName)).thenReturn(Optional.empty());
        var exception = assertThrows(CategoryNotFoundException.class,
                () -> productService.addCategoryToProduct(p0.getProductCode(), categoryName));
        assertEquals(String.format(CATEGORY_NOT_FOUND, categoryName), exception.getMessage());
    }
}
