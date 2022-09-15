package com.shopapp.shopApp.exception.role;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RoleExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(RoleExistsException.class)
    public ResponseEntity<Object> handleRoleExistsException(RoleExistsException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

}
