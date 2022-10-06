package com.shopapp.shopApp.AppUserTests.ProductTests;

import com.shopapp.shopApp.model.Category;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.repository.CategoryRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository productRepo;
    private Category category;
    private Product product;
    @BeforeEach
    void setUp() {
        this.category = new Category(
                null,
                "Motorization",
                "Category: Motorization",
                "test.jpg"
        );

        categoryRepo.save(category);

        this.product = new Product(
                null,
                UUID.randomUUID().toString(),
                "Test product",
                "Test description",
                99.99,
                30,
                "test.jpg",
                new ArrayList<>(),
                new ArrayList<>()
        );

        product.getCategories().add(category);
        productRepo.save(product);
    }

    @Test
    void itShouldFindProductByProductCode() {
        // when
        Optional<Product> foundProduct = productRepo.findByProductCode(product.getProductCode());
        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct).isInstanceOf(Optional.class);
        assertThat(foundProduct.get()).isInstanceOf(Product.class);
    }

    @Test
    void itShouldNotFindAnyProductByProductCode() {
        // when
        Optional<Product> foundProduct = productRepo.findByProductCode(anyString());
        // then
        assertThat(foundProduct).isNotPresent();
    }

    @Test
    void itChecksIfProductExistsByProductCode() {
        // when
        Boolean expected = productRepo.existsByProductCode(product.getProductCode());
        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itChecksIfProductDoesNotExistsByProductCode() {
        // when
        Boolean expected = productRepo.existsByProductCode(anyString());
        // then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldFindAllProductsWithCategoriesJoined() {
        // when
        List<Product> allProducts = productRepo.findAll();
        // then
        assertThat(allProducts).isNotNull();
        assertThat(allProducts.size()).isNotZero();
    }

    @Test
    void itShouldFindAllProductsWithPageableAndWithoutCategoriesJoined() {
        // when
        List<Product> allProducts = productRepo.findAllProducts(PageRequest.of(0, 5));
        // then
        assertThat(allProducts).isNotNull();
        assertThat(allProducts.size()).isNotZero();
    }
}