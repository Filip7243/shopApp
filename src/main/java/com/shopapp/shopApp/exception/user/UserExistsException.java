package com.shopapp.shopApp.exception.user;

public class UserExistsException extends RuntimeException{

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
