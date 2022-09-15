package com.shopapp.shopApp.exception.user;

public class UserCodeNotFoundException extends RuntimeException{

    public UserCodeNotFoundException(String message) {
        super(message);
    }

    public UserCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
