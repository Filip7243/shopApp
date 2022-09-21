package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.ProductDisplayDto;
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

import static com.shopapp.shopApp.mapper.ProductMapper.getSetOfProductsDto;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListServiceImpl wishListService;
    private final JwtUtils jwtUtils;
    private final AppUserServiceImpl userService;

    @GetMapping("/show")
    public ResponseEntity<Set<ProductDisplayDto>> showWishListProducts(@RequestParam String wishListCode) {
        try {
            Set<ProductDisplayDto> productDto = getSetOfProductsDto(wishListService.getProducts(wishListCode));
            return ResponseEntity.ok(productDto);
        } catch (WishListNotFoundException e) {
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWishList(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        AppUser user = (AppUser) userService.loadUserByUsername(username);
        wishListService.createWishList(user.getUserCode());
        return ResponseEntity.created(URI.create(request.getRequestURI())).body("WishList created!");
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToWishList(@RequestParam String wishListCode, @RequestParam String productCode, HttpServletRequest request) {
        try {
            wishListService.addProductToWishList(wishListCode, productCode, request);
            return ResponseEntity.ok("Added item!");
        } catch (WishListNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deleteItem")
    public ResponseEntity<?> deleteProductFromWishList(@RequestParam String wishListCode, @RequestParam String productCode) {
        try {
            wishListService.deleteProductFromWishList(wishListCode, productCode);
            return ResponseEntity.ok("Product deleted from wishList");
        } catch (WishListNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteWishList(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromHeader(request);
            String username = jwtUtils.getUsernameFromJwtToken(token);
            AppUser user = (AppUser) userService.loadUserByUsername(username);
            wishListService.deleteWishList(user);
            return ResponseEntity.ok("WishList deleted!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
