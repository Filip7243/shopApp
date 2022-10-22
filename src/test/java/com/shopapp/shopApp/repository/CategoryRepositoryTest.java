package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepo;
    private Category category;

    @BeforeEach
    void setUp() {
        this.category = new Category(
                null,
                "Motorization",
                "Category: Motorization",
                "test.jpg"
        );

        categoryRepo.save(category);
    }

    @Test
    void itShouldFindCategoryByName() {
        // when
        Optional<Category> foundCategory = categoryRepo.findByCategoryName(category.getCategoryName());
        // then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory).isInstanceOf(Optional.class);
        assertThat(foundCategory.get()).isInstanceOf(Category.class);
    }

    @Test
    void itShouldNotFindCategoryByName() {
        // when
        Optional<Category> foundCategory = categoryRepo.findByCategoryName(anyString());
        // then
        assertThat(foundCategory).isNotPresent();
    }
    @Test
    void itChecksIfCategoryExistsByName() {
        // when
        boolean expected = categoryRepo.existsByCategoryName(category.getCategoryName());
        // then
        assertThat(expected).isTrue();
    }
    @Test
    void itChecksIfCategoryDoesNotExistsByName() {
        // when
        boolean expected = categoryRepo.existsByCategoryName(anyString());
        // then
        assertThat(expected).isFalse();
    }
}