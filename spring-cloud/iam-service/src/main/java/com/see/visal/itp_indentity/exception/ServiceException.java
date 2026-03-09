package com.see.visal.itp_indentity.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the IAM (Identity & Access Management) service.
 * Carries an HTTP status so the global handler can map it directly.
 */
public class ServiceException extends RuntimeException {

    private final HttpStatus status;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // Convenience factories
    public static ServiceException unauthorized(String message) {
        return new ServiceException(HttpStatus.UNAUTHORIZED, message);
    }

    public static ServiceException forbidden(String message) {
        return new ServiceException(HttpStatus.FORBIDDEN, message);
    }

    public static ServiceException notFound(String message) {
        return new ServiceException(HttpStatus.NOT_FOUND, message);
    }

    public static ServiceException badRequest(String message) {
        return new ServiceException(HttpStatus.BAD_REQUEST, message);
    }

    public static ServiceException conflict(String message) {
        return new ServiceException(HttpStatus.CONFLICT, message);
    }

    public static ServiceException internalError(String message) {
        return new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ServiceException internalError(String message, Throwable cause) {
        return new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
