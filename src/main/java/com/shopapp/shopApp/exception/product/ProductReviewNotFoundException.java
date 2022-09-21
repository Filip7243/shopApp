package com.shopapp.shopApp.exception.product;

public class ProductReviewNotFoundException extends RuntimeException{

    public ProductReviewNotFoundException(String message) {
        super(message);
    }

    public ProductReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
