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

}
