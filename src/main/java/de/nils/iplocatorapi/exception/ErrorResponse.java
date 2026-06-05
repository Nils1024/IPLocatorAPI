package de.nils.iplocatorapi.exception;

public record ErrorResponse(int status, String message, long timestamp) {
}
