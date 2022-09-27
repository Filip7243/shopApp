package com.shopapp.shopApp.service.wishlist;

import com.shopapp.shopApp.constants.ExceptionsConstants;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.repository.WishListRepository;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.product.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.shopapp.shopApp.constants.ExceptionsConstants.WISH_LIST_NOT_FOUND;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final AppUserServiceImpl appUserService;
    private final ProductServiceImpl productService;
    private final JwtUtils jwtUtils;

    @Override
    public String createWishList(String userCode) {
        WishList wishList = new WishList();
        wishList.setWishListCode(UUID.randomUUID().toString());
        wishList.setUser(appUserService.getUserWithUserCode(userCode));
        wishList.setWishListItems(new HashSet<>());
        wishListRepository.save(wishList);
        return wishList.getWishListCode();
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
    public void addProductToWishList(String wishListCode, String productCode, HttpServletRequest request) {
        AppUser user = null;
        if(!wishListRepository.existsByWishListCode(wishListCode)) {
            String token = jwtUtils.getTokenFromHeader(request);
            String username = jwtUtils.getUsernameFromJwtToken(token);
            user = (AppUser) appUserService.loadUserByUsername(username);
            wishListCode = createWishList(user.getUserCode());
        }//TODO: reformat

        WishList wishList = getWishList(wishListCode);
        Set<Product> wishListItems = wishList.getWishListItems();
        Product product = productService.getProductWithProductCode(productCode);
        if(wishListItems.contains(product)) {
            return;
        }
        wishListItems.add(product);
        wishListRepository.save(wishList);
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
