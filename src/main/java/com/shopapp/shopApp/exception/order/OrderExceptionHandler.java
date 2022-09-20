package com.shopapp.shopApp.exception.order;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }
}
