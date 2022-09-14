package com.shopapp.shopApp;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAppApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserServiceImpl userService) {

		return args -> {
			userService.saveUser(new AppUserSaveUpdateDto(
					"Filip",
					"Kaczmarczyk",
					"filip7243@gmail.com",
					"1234",
					"123456789",
					"adres"
			));
		};
	}

}
