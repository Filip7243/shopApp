package com.shopapp.shopApp.exception.product;

import com.shopapp.shopApp.exception.ExceptionDetails;
import com.shopapp.shopApp.exception.category.CategoryExistsException;
import com.shopapp.shopApp.exception.category.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(CategoryExistsException.class)
    public ResponseEntity<Object> handleCategoryExistsException(CategoryExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), CONFLICT);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ProductExistsException.class)
    public ResponseEntity<Object> handleProductExistsException(ProductExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), CONFLICT);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Object> handleCartItemNotFoundException(CartItemNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<Object> handleShoppingCartNotFoundException(ShoppingCartNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(NotEnoughInStockException.class)
    public ResponseEntity<Object> handleNotEnoughInStockException(NotEnoughInStockException e) {
        ExceptionDetails details = createDetails(e.getMessage(), CONFLICT);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ProductReviewNotFoundException.class)
    public ResponseEntity<Object> handleProductReviewNotFoundException(ProductReviewNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }
}
