package com.shopapp.shopApp.exception.wishlist;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class WishListExceptionHandler {

    @ExceptionHandler(WishListNotFoundException.class)
    public ResponseEntity<Object> handleWishListNotFoundException(WishListNotFoundException e) {
        ExceptionDetails details = ExceptionDetails.createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }
}
