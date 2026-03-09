package com.see.visal.account_service.rest.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the Account Service.
 * Carries an HTTP status so AccountExceptionHandler can map it to the correct HTTP response.
 */
public class AccountException extends RuntimeException {

    private final HttpStatus status;

    public AccountException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public AccountException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public static AccountException notFound(String message) {
        return new AccountException(HttpStatus.NOT_FOUND, message);
    }

    public static AccountException badRequest(String message) {
        return new AccountException(HttpStatus.BAD_REQUEST, message);
    }

    public static AccountException conflict(String message) {
        return new AccountException(HttpStatus.CONFLICT, message);
    }

    public static AccountException forbidden(String message) {
        return new AccountException(HttpStatus.FORBIDDEN, message);
    }

    public static AccountException internalError(String message) {
        return new AccountException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static AccountException internalError(String message, Throwable cause) {
        return new AccountException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
