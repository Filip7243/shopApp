package com.shopapp.shopApp.exception.token;

public class ConfirmationTokenExpiredException extends RuntimeException{

    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }

    public ConfirmationTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
