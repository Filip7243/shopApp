package com.shopapp.shopApp.exception.product;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(CategoryExistsException.class)
    public ResponseEntity<Object> handleCategoryExistsException(CategoryExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ProductExistsException.class)
    public ResponseEntity<Object> handleProductExistsException(ProductExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Object> handleCartItemNotFoundException(CartItemNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<Object> handleShoppingCartNotFoundException(ShoppingCartNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(NotEnoughInStockException.class)
    public ResponseEntity<Object> handleNotEnoughInStockException(NotEnoughInStockException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }
}
