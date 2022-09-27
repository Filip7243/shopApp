package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.wishlist.WishListServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static com.shopapp.shopApp.mapper.ProductMapper.getSetOfProductsDto;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/wishlists")
public class WishListController {

    private final WishListServiceImpl wishListService;
    private final JwtUtils jwtUtils;
    private final AppUserServiceImpl userService;

    @GetMapping("/show")
    public ResponseEntity<Set<ProductDisplayDto>> showWishListProducts(@RequestParam String wishListCode) throws WishListNotFoundException {
        return ResponseEntity.ok(getSetOfProductsDto(wishListService.getProducts(wishListCode)));
    }//todo; ogarnac jak zrobic zeby katergorie wyswetlalo w jednym zapytaniu

    @PostMapping("/create")
    public ResponseEntity<?> createWishList(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        AppUser user = (AppUser) userService.loadUserByUsername(username);
        wishListService.createWishList(user.getUserCode());
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(WISH_LIST_CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToWishList(@RequestParam String wishListCode, @RequestParam String productCode, HttpServletRequest request)
            throws WishListNotFoundException, UserNotFoundException, ProductNotFoundException, CategoryNotFoundException {

        wishListService.addProductToWishList(wishListCode, productCode, request);
        return ResponseEntity.ok(String.format(WISH_LIST_PRODUCT_ADDED, productCode));
    }

    @PostMapping("/deleteItem")
    public ResponseEntity<?> deleteProductFromWishList(@RequestParam String wishListCode, @RequestParam String productCode)
            throws WishListNotFoundException, ProductNotFoundException, CategoryNotFoundException {

        wishListService.deleteProductFromWishList(wishListCode, productCode);
        return ResponseEntity.ok(String.format(WISH_LIST_PRODUCT_DELETED, productCode));
    }//TODO: shopping cart po strone usera w mouelu, o tego przy wyswietlaniu itemow mniej inforamcji(koszyk), itd..

    @DeleteMapping("/delete") //todo: do zastanowienia czy ten endpoint jest potrzebny
    public ResponseEntity<?> deleteWishList(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        AppUser user = (AppUser) userService.loadUserByUsername(username);
        wishListService.deleteWishList(user);
        return ResponseEntity.ok(WISH_LIST_DELETED);
    }
}
