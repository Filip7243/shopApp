package com.shopapp.shopApp.exception.token;

public class ConfirmationTokenConfirmedException extends RuntimeException{

    public ConfirmationTokenConfirmedException(String message) {
        super(message);
    }

    public ConfirmationTokenConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }
}
