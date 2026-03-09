package com.see.visal.customer_service.rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Uniform error response body returned by the Customer Service.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        ZonedDateTime timestamp,
        List<FieldError> fieldErrors
) {
    /** Per-field validation error detail. */
    public record FieldError(String field, String message) {}
}

