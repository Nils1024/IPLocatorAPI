package de.nils.iplocatorapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RateLimitExceptionHandler {
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handle(RateLimitException e, HttpServletRequest request) {
        ErrorResponse errorResponse = e.getErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
}
