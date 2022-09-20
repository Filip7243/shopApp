package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.ProductDisplayDto;
import com.shopapp.shopApp.exception.wishlist.WishListNotFoundException;
import com.shopapp.shopApp.mapper.ProductMapper;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.Product;
import com.shopapp.shopApp.model.WishList;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import com.shopapp.shopApp.service.WishListServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static com.shopapp.shopApp.mapper.ProductMapper.getProductsDto;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
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
            Set<ProductDisplayDto> productDto = getProductsDto(wishListService.getProducts(wishListCode));
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
    public ResponseEntity<?> addProductToWishList(@RequestParam String wishListCode, @RequestParam String productCode) {
        try {
            wishListService.addProductToWishList(wishListCode, productCode);
            return ResponseEntity.ok("Added item!");
        } catch (WishListNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
