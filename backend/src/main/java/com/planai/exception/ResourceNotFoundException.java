package com.planai.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * Exception thrown when a requested resource cannot be found.
 * Returns HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends ApiException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s not found with id: %s", resourceName, id), STATUS, ERROR_CODE);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id), STATUS, ERROR_CODE);
    }
}
