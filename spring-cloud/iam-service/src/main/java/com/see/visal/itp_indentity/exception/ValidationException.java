package com.see.visal.itp_indentity.exception;

import java.util.List;

/**
 * Thrown when incoming request data fails validation in the IAM service.
 * Wraps per-field error details so the handler can report them individually.
 */
public class ValidationException extends RuntimeException {

    private final List<FieldError> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = List.of();
    }

    public ValidationException(String message, List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? fieldErrors : List.of();
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    /** Per-field validation error detail. */
    public record FieldError(String field, String message) {}
}
