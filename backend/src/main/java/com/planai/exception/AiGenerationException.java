package com.planai.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the AI service fails to generate a response.
 * Returns HTTP 503 Service Unavailable.
 */
public class AiGenerationException extends ApiException {

    private static final HttpStatus STATUS = HttpStatus.SERVICE_UNAVAILABLE;
    private static final String ERROR_CODE = "AI_GENERATION_ERROR";

    public AiGenerationException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public AiGenerationException(String message, Throwable cause) {
        super(message, STATUS, ERROR_CODE);
        initCause(cause);
    }
}
