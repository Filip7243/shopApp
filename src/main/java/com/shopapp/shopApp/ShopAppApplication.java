package com.shopapp.shopApp;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.ProductSaveUpdateDto;
import com.shopapp.shopApp.mapper.ProductMapper;
import com.shopapp.shopApp.model.*;
import com.shopapp.shopApp.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class ShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAppApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserServiceImpl userService, ShoppingCartServiceImpl cartService,
						  CartItemServiceImpl cartItemService, ProductServiceImpl productService, CategoryServiceImpl categoryService) {

		return args -> {
			AppUser user = userService.saveUser(new AppUserSaveUpdateDto(
					"Filip",
					"Kaczmarczyk",
					"filip7243@gmail.com",
					"1234",
					"123456789",
					"adres"
			));
			Category category = new Category(null, "Books", "Books category", "abc.com");
			categoryService.addCategory(category);
			Product product = new Product(null, UUID.randomUUID().toString(), "Book", "This is a book product", 22.40, 11, "dsab.com", category);
			productService.addProduct(product);

		};
	}

}
