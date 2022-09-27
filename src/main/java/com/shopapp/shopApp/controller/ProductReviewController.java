package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.constants.ResponseConstants;
import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.exception.product.ProductNotFoundException;
import com.shopapp.shopApp.exception.product.ProductReviewNotFoundException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ProductReview;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.product.ProductReviewServiceImpl;
import com.shopapp.shopApp.service.product.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static com.shopapp.shopApp.mapper.ProductReviewMapper.mapToProductReview;
import static org.springframework.http.HttpStatus.GONE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product/reviews")
public class ProductReviewController {

    private final ProductReviewServiceImpl reviewService;
    private final JwtUtils jwtUtils;
    private final AppUserServiceImpl appUserService;
    private final ProductServiceImpl productService;

    @GetMapping("/show")
    public List<ProductReviewAddUpdateDto> getUserReviews(HttpServletRequest request) throws UserNotFoundException {
        AppUser user = getUser(request);
        return reviewService.getUserReviews(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody @Valid ProductReviewAddUpdateDto reviewDto,
                                       @RequestParam String productCode,
                                       HttpServletRequest request) throws ProductNotFoundException, UserNotFoundException {

        ProductReview productReview = mapToProductReview(reviewDto);
        productReview.setProductId(productService.getProductWithProductCode(productCode).getId());
        AppUser user = getUser(request);
        productReview.setUser(user);
        reviewService.addReview(productReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format(PRODUCT_REVIEW_CREATED, productCode, user.getEmail()));
        //TODO: prevent multiple reviews
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@RequestParam String reviewCode,
                                          @RequestBody @Valid ProductReviewAddUpdateDto reviewDto) throws ProductReviewNotFoundException {
        reviewService.updateReview(reviewCode, reviewDto);
        return ResponseEntity.ok().body(String.format(PRODUCT_REVIEW_UPDATED, reviewCode));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam String reviewCode) throws ProductReviewNotFoundException {
        reviewService.deleteReview(reviewCode);
        return ResponseEntity.status(GONE).body(String.format(PRODUCT_REVIEW_DELETED, reviewCode));
    }

    private AppUser getUser(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        return (AppUser) appUserService.loadUserByUsername(username);
    }
}
