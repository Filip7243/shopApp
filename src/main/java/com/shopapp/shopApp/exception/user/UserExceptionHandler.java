package com.shopapp.shopApp.exception.user;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Object> handleUserExistsException(UserExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), CONFLICT);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(UserCodeNotFoundException.class)
    public ResponseEntity<Object> handleUserCodeNotFoundException(UserCodeNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(BadEmailException.class)
    public ResponseEntity<Object> handleUserCodeNotFoundException(BadEmailException e) {
        ExceptionDetails details = createDetails(e.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(details, details.status());
    }

}
