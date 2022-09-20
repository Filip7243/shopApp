package com.shopapp.shopApp.exception.wishlist;

public class WishListNotFoundException extends RuntimeException{

    public WishListNotFoundException(String message) {
        super(message);
    }

    public WishListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
