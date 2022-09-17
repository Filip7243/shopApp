package com.shopapp.shopApp.exception.product;

public class ShoppingCartNotFoundException extends RuntimeException{

    public ShoppingCartNotFoundException(String message) {
        super(message);
    }

    public ShoppingCartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
