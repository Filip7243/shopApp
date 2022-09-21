package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.ProductReviewAddUpdateDto;
import com.shopapp.shopApp.exception.product.ProductReviewNotFoundException;
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
import java.util.List;

import static com.shopapp.shopApp.mapper.ProductReviewMapper.mapToProductReview;
import static org.springframework.http.HttpStatus.GONE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product/review")
public class ProductReviewController {

    private final ProductReviewServiceImpl reviewService;
    private final JwtUtils jwtUtils;
    private final ProductServiceImpl productService;
    private final AppUserServiceImpl appUserService;

    @GetMapping("/show")
    public List<ProductReviewAddUpdateDto> getUserReviews(HttpServletRequest request) {
        AppUser user = getUser(request);
        return reviewService.getUserReviews(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ProductReviewAddUpdateDto reviewDto,
                                       @RequestParam String productCode,
                                       HttpServletRequest request) {
        ProductReview productReview = mapToProductReview(reviewDto);
        productReview.setProduct(productService.getProductWithProductCode(productCode));
        AppUser user = getUser(request);
        productReview.setUser(user);
        reviewService.addReview(productReview);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created!");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@RequestParam String reviewCode,
                                          @RequestBody ProductReviewAddUpdateDto reviewDto) {
        try {
            reviewService.updateReview(reviewCode, reviewDto);
            return ResponseEntity.ok().body("Review Updated!");
        } catch (ProductReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam String reviewCode) {
        try {
            reviewService.deleteReview(reviewCode);
            return ResponseEntity.status(GONE).body("Deleted review!");
        } catch (ProductReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private AppUser getUser(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromJwtToken(token);
        return (AppUser) appUserService.loadUserByUsername(username);
    }
}
