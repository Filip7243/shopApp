package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUser;

public interface WishListService {

    void createWishList(String userCode);

    void deleteProductFromWishList(String wishListCode, String productCode);

    void deleteWishList(AppUser user);

    void addProductToWishList(String wishListCode, String productCode);

}
