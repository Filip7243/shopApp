package com.shopapp.shopApp.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;


public record ExceptionDetails(String message, HttpStatus status, ZonedDateTime timestamp) {

    public static ExceptionDetails createDetails(String e, HttpStatus status) {
        return new ExceptionDetails(e, status, ZonedDateTime.now(ZoneId.of("Z")));
    }
}
