package com.shopapp.shopApp.exception.token;

import com.shopapp.shopApp.exception.ExceptionDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.shopapp.shopApp.exception.ExceptionDetails.createDetails;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public ResponseEntity<Object> handleConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException e) {
        ExceptionDetails details = createDetails(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ConfirmationTokenConfirmedException.class)
    public ResponseEntity<Object> handleConfirmationTokenConfirmedException(ConfirmationTokenConfirmedException e) {
        ExceptionDetails details = createDetails(e.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(ConfirmationTokenExpiredException.class)
    public ResponseEntity<Object> handleConfirmationTokenExpiredException(ConfirmationTokenExpiredException e) {
        ExceptionDetails details = createDetails(e.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(details, details.status());
    }

    @ExceptionHandler(PasswordResetTokenException.class)
    public ResponseEntity<Object> handlePasswordResetTokenException(PasswordResetTokenException e) {
        ExceptionDetails details = createDetails(e.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(details, details.status());
    }

}
