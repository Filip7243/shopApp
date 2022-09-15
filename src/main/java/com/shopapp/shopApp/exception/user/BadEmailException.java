package com.shopapp.shopApp.exception.user;

public class BadEmailException extends RuntimeException{

    public BadEmailException(String message) {
        super(message);
    }

    public BadEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
