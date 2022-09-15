package com.shopapp.shopApp.exception.role;

public class RoleExistsException extends RuntimeException{

    public RoleExistsException(String message) {
        super(message);
    }

    public RoleExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
