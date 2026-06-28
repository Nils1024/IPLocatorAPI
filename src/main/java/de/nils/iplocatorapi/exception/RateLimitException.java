package de.nils.iplocatorapi.exception;

import org.springframework.http.HttpStatus;

public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }

    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded", System.currentTimeMillis());
    }
}
