package com.shopapp.shopApp.exception.category;

public class CategoryExistsException extends RuntimeException{

    public CategoryExistsException(String message) {
        super(message);
    }

    public CategoryExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
