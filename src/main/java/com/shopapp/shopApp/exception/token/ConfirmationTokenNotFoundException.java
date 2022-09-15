package com.shopapp.shopApp.exception.token;

public class ConfirmationTokenNotFoundException extends RuntimeException {

    public ConfirmationTokenNotFoundException(String message) {
        super(message);
    }

    public ConfirmationTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
