package com.shopapp.shopApp;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.*;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.cartitem.CartItemServiceImpl;
import com.shopapp.shopApp.service.category.CategoryServiceImpl;
import com.shopapp.shopApp.service.product.ProductReviewServiceImpl;
import com.shopapp.shopApp.service.product.ProductServiceImpl;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class ShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAppApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserServiceImpl userService, ProductServiceImpl productService, CategoryServiceImpl categoryService,
						  ProductReviewServiceImpl reviewService) {

		return args -> {
//			AppUser user = userService.saveUser(new AppUserSaveUpdateDto(
//					"Filip",
//					"Kaczmarczyk",
//					"filip7243@gmail.com",
//					"1234",
//					"123456789",
//					"adres"
//			));
//			Category category = new Category(null, "Books", "Books category", "abc.com");
//			Category category2 = new Category(null, "Books2", "Books category2", "abc.com222");
//			categoryService.addCategory(category);
//			categoryService.addCategory(category2);
//			Product product = new Product(null, UUID.randomUUID().toString(), "Book", "This is a book product", 22.40, 11, "dsab.com", new ArrayList<>(), new ArrayList<>());
//			Product product2 = new Product(null, UUID.randomUUID().toString(), "dsadas", "This iscxzcduct", 22.22, 15, "d3b.com", new ArrayList<>(), new ArrayList<>());
//
//			ProductReview review = new ProductReview(null, UUID.randomUUID().toString(), "ESSA", "SDAHDGAS", 2, 1L, user);
//			ProductReview review2 = new ProductReview(null, UUID.randomUUID().toString(), "ESSA", "SDAHDGAS", 2,  1L, user);
//			ProductReview review3 = new ProductReview(null, UUID.randomUUID().toString(), "ESdsadsadA", "SDdsadcxzAS", 2, 1L, user);
//			ProductReview review4 = new ProductReview(null, UUID.randomUUID().toString(), "ESdsadsadA", "SDdsadcxzAS", 2, 2L, user);
//			reviewService.addReview(review);
//			reviewService.addReview(review2);
//			reviewService.addReview(review3);
//			reviewService.addReview(review4);
//			product.getCategories().add(category);
//			product.getCategories().add(category2);
//
//			product2.getCategories().add(category2);
//			productService.addProduct(product);
//			productService.addProduct(product2);



		};
	}

}
