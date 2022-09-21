package com.shopapp.shopApp.constants;

public class ResponseConstants {

    // USER
    public static final String USER_CREATED = "User with email: %s has been created!";
    public static final String USER_DELETED = "User with code: %s has been deleted!";
    public static final String USER_UPDATED = "User with code: %s has been updated!";
    public static final String ROLE_ADDED_TO_USER = "Role: %s has been added to user with code: %s";
    public static final String ACCESS_TOKEN_NOT_REFRESHED = "Access token has not been refreshed! Try again!";

    // ROLE
    public static final String ROLE_CREATED = "Role: %s has been created!";
    public static final String ROLE_DELETED = "Role: %s has been deleted!";
    public static final String ROLE_UPDATED = "Role: %s has been updated!";

    // AUTH
    public static final String USER_REGISTERED = "User with email: %s has been registered!";
    public static final String EMAIL_CONFIRMED = "Email has been confirmed!";

    // CATEGORY
    public static final String CATEGORY_CREATED = "Category: %s has been created!";
    public static final String CATEGORY_UPDATED = "Category: %s has been updated!";
    public static final String CATEGORY_DELETED = "Category has been deleted!";

    // ORDER
    public static final String ORDER_DELETED = "Order with code: %s has been deleted!";

    // PRODUCT
    public static final String PRODUCT_CREATED = "Product: %s has been created!";
    public static final String CATEGORY_ADDED_TO_PRODUCT = "Category: %s has been added to product with code: %s";
    public static final String PRODUCT_UPDATED = "Product with code: %s has been updated!";
    public static final String PRODUCT_DELETED = "Product with code: %s has been deleted!";

    // PRODUCT REVIEW
    public static final String PRODUCT_REVIEW_CREATED = "Review of product: %s has been created with account: %s";
    public static final String PRODUCT_REVIEW_UPDATED = "Review with code: %s has been updated!";
    public static final String PRODUCT_REVIEW_DELETED = "Review with code: %s has been deleted!";

    // SHOPPING CART
    public static final String USER_ADDED_TO_SHOPPING_CART = "User: %s added to shopping cart with code: %s";
    public static final String SHOPPING_CART_ITEM_ADDED = "Product with code: %s added to shopping cart with code: %s";
    public static final String SHOPPING_CART_ITEM_DELETED = "Item with id: %s has been deleted!";

    // WISHLIST
    public static final String WISH_LIST_CREATED = "Wishlist has been created!";
    public static final String WISH_LIST_PRODUCT_ADDED = "Product with code: %s has been added to wishlist!";
    public static final String WISH_LIST_PRODUCT_DELETED = "Product with code: %s has been deleted from wishlist!";
    public static final String WISH_LIST_DELETED = "Wishlist has been deleted!";
}
