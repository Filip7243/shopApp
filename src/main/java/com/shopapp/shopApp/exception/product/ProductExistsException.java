package com.shopapp.shopApp.exception.product;

public class ProductExistsException extends RuntimeException{

    public ProductExistsException(String message) {
        super(message);
    }

    public ProductExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
