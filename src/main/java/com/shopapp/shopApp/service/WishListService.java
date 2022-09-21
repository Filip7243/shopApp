package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface WishListService {

    String createWishList(String userCode);

    void deleteProductFromWishList(String wishListCode, String productCode);

    void deleteWishList(AppUser user);

    void addProductToWishList(String wishListCode, String productCode, HttpServletRequest request);

    Set<Product> getProducts(String wishListCode);

}
