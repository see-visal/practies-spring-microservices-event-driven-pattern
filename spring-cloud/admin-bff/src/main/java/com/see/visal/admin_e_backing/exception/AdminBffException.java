package com.see.visal.admin_e_backing.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the Admin BFF service.
 */
public class AdminBffException extends RuntimeException {

    private final HttpStatus status;

    public AdminBffException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public AdminBffException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // Convenience factories
    public static AdminBffException unauthorized(String message) {
        return new AdminBffException(HttpStatus.UNAUTHORIZED, message);
    }

    public static AdminBffException forbidden(String message) {
        return new AdminBffException(HttpStatus.FORBIDDEN, message);
    }

    public static AdminBffException notFound(String message) {
        return new AdminBffException(HttpStatus.NOT_FOUND, message);
    }

    public static AdminBffException badRequest(String message) {
        return new AdminBffException(HttpStatus.BAD_REQUEST, message);
    }

    public static AdminBffException internalError(String message) {
        return new AdminBffException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static AdminBffException internalError(String message, Throwable cause) {
        return new AdminBffException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

