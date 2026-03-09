package com.see.visal.customer_service.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the Customer Service.
 */
public class CustomerException extends RuntimeException {

    private final HttpStatus status;

    public CustomerException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CustomerException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // ── Convenience factories ─────────────────────────────────────────────────

    public static CustomerException notFound(String message) {
        return new CustomerException(HttpStatus.NOT_FOUND, message);
    }

    public static CustomerException badRequest(String message) {
        return new CustomerException(HttpStatus.BAD_REQUEST, message);
    }

    public static CustomerException conflict(String message) {
        return new CustomerException(HttpStatus.CONFLICT, message);
    }

    public static CustomerException internalError(String message) {
        return new CustomerException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static CustomerException internalError(String message, Throwable cause) {
        return new CustomerException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

