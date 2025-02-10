package com.mental.cove.exception;

import com.mental.cove.common.HttpStatusCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<String> handleRateLimitExceeded(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatusCodes.SC_TOO_MANY_REQUESTS).body(ex.getMessage());
    }
    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<String> handleRateLimitExceeded(TokenValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<String> handCustomerBusinessException(CustomBusinessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}