package com.shopapp.shopApp.constants;

public class ExceptionsConstants {

    // USER
    public static final String BAD_EMAIL = "Email: %s in not valid!";
    public static final String USER_CODE_NOT_FOUND = "User with user code: %s not found!";
    public static final String USER_ALREADY_EXISTS = "User with email: %s, already exists!";
    public static final String USER_NOT_FOUND = "User %s not found!";

    // CONFIRMATION TOKEN
    public static final String TOKEN_CONFIRMED = "Token %s already confirmed!";
    public static final String TOKEN_EXPIRED = "Token expired at: %s!";
    public static final String TOKEN_NOT_FOUND = "Token not found!";

    // ROLE
    public static final String ROLE_ALREADY_EXISTS = "Role %s already exists!";
    public static final String ROLE_NOT_FOUND = "Role %s not found!";

    // CATEGORY
    public static final String CATEGORY_ALREADY_EXISTS = "Category %s already exists!";
    public static final String CATEGORY_NOT_FOUND = "Category %s not found!";

    // CART ITEM
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found!";
    public static final String NOT_ENOUGH_IN_STOCK = "Not enough item in stock!";

    // PRODUCT
    public static final String PRODUCT_ALREADY_EXISTS = "Product %s already exists;";
    public static final String PRODUCT_NOT_FOUND = "Product %s not found!";

    // SHOPPING CART
    public static final String SHOPPING_CART_NOT_FOUND = "Shopping cart not found!";
}
