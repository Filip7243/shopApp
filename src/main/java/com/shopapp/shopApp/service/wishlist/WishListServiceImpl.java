package com.shopapp.shopApp.service.wishlist;

import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ProductRepository;
import com.shopapp.shopApp.repository.WishListRepository;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final AppUserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtUtils jwtUtils;

    @Override
    public String createWishList(String userCode) {
        WishList wishList = new WishList();
        wishList.setWishListCode(UUID.randomUUID().toString());
        wishList.setUser(userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UserCodeNotFoundException(String.format(USER_CODE_NOT_FOUND, userCode))));
        wishList.setWishListItems(new HashSet<>());
        wishListRepository.save(wishList);
        return wishList.getWishListCode();
    }

    @Override
    public void deleteProductFromWishList(String wishListCode, String productCode) {
        WishList wishList = getWishList(wishListCode);
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, "with code: " + productCode)));
        wishList.getWishListItems().remove(product);
        wishListRepository.save(wishList);
    }

    @Override
    public void deleteWishList(AppUser user) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, user.getEmail())));
        wishListRepository.delete(wishList);
    }

    @Override
    public void addProductToWishList(String wishListCode, String productCode, HttpServletRequest request) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, "with code: " + productCode)));
        WishList wishList;

        try {
            wishList = getWishList(wishListCode);
        } catch (WishListNotFoundException e) {
            wishList = new WishList();
            String token = jwtUtils.getTokenFromHeader(request);
            String username = jwtUtils.getUsernameFromJwtToken(token);
            AppUser user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, username)));
            wishList.setWishListCode(UUID.randomUUID().toString());
            wishList.setUser(user);
            wishList.setWishListItems(new HashSet<>());
        }

        assert wishList != null;
        Set<Product> wishListItems = wishList.getWishListItems();
        wishListItems.add(product);
        wishListRepository.save(wishList);

//        if (!wishListRepository.existsByWishListCode(wishListCode)) {
//            String token = jwtUtils.getTokenFromHeader(request);
//            String username = jwtUtils.getUsernameFromJwtToken(token);
//            AppUser user = userRepository.findByEmail(username)
//                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, username)));
//            wishList = new WishList();
//            wishList.setWishListCode(UUID.randomUUID().toString());
//            wishList.setUser(user);
//            wishList.setWishListItems(new HashSet<>());
//            wishListRepository.save(wishList);
//        }

//        wishList = getWishList(wishListCode);
//        Set<Product> wishListItems = wishList.getWishListItems();
//        Product product = productRepository.findByProductCode(productCode)
//                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, "with code: " + productCode)));
//        wishListItems.add(product);
//        wishListRepository.save(wishList);
    }

    @Override
    public Set<Product> getProducts(String wishListCode) {
        return getWishList(wishListCode).getWishListItems();
    }

    public WishList getWishList(String wishListCode) {
        return wishListRepository.findByWishListCode(wishListCode)
                .orElseThrow(() -> new WishListNotFoundException(WISH_LIST_NOT_FOUND));
    }
}
