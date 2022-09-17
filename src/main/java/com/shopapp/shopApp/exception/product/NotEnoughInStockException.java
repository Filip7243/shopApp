package com.shopapp.shopApp.exception.product;

public class NotEnoughInStockException extends RuntimeException{

    public NotEnoughInStockException(String message) {
        super(message);
    }

    public NotEnoughInStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
