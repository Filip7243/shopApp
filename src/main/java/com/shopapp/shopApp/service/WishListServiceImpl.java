package com.shopapp.shopApp.service;

import com.shopapp.shopApp.constants.ExceptionsConstants;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.repository.WishListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final AppUserServiceImpl appUserService;
    private final ProductServiceImpl productService;

    @Override
    public void createWishList(String userCode) {
        WishList wishList = new WishList();
        wishList.setUser(appUserService.getUserWithUserCode(userCode));
        wishList.setWishListItems(new HashSet<>());
        wishListRepository.save(wishList);
    }

    @Override
    public void deleteProductFromWishList(String wishListCode, String productCode) {
        WishList wishList = getWishList(wishListCode);
        Product product = productService.getProductWithProductCode(productCode);
        wishList.getWishListItems().remove(product);
        wishListRepository.save(wishList);
    }

    @Override
    public void deleteWishList(AppUser user) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionsConstants.USER_NOT_FOUND, user.getEmail())));
        wishListRepository.delete(wishList);
    }

    @Override
    public void addProductToWishList(String wishListCode, String productCode) {
        WishList wishList = getWishList(wishListCode);
        Product product = productService.getProductWithProductCode(productCode);
        wishList.getWishListItems().add(product);
        wishListRepository.save(wishList);
    }

    private WishList getWishList(String wishListCode) {
        return wishListRepository.findByWishListCode(wishListCode)
                .orElseThrow(() -> new WishListNotFound(ExceptionsConstants.WISH_LIST_NOT_FOUND));
    }
}
