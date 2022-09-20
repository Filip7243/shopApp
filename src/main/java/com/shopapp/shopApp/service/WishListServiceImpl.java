package com.shopapp.shopApp.service;

import com.shopapp.shopApp.constants.ExceptionsConstants;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.repository.WishListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.WISH_LIST_NOT_FOUND;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final AppUserServiceImpl appUserService;
    private final ProductServiceImpl productService;

    @Override
    public void createWishList(String userCode) {
        WishList wishList = new WishList();
        wishList.setWishListCode(UUID.randomUUID().toString());
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
        //TODO: zrobic if w ktorym sprawdzac czy instnieje shish list jestli nie to uzywam metoty createWishList(usercode)
    }

    @Override
    public Set<Product> getProducts(String wishListCode) {
        return getWishList(wishListCode).getWishListItems();
    }

    private WishList getWishList(String wishListCode) {
        return wishListRepository.findByWishListCode(wishListCode)
                .orElseThrow(() -> new WishListNotFoundException(WISH_LIST_NOT_FOUND));
    }
}
