package com.see.visal.front_bff.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the Front BFF service.
 */
public class FrontBffException extends RuntimeException {

    private final HttpStatus status;

    public FrontBffException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public FrontBffException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // ── Convenience factories ─────────────────────────────────────────────────

    public static FrontBffException unauthorized(String message) {
        return new FrontBffException(HttpStatus.UNAUTHORIZED, message);
    }

    public static FrontBffException forbidden(String message) {
        return new FrontBffException(HttpStatus.FORBIDDEN, message);
    }

    public static FrontBffException notFound(String message) {
        return new FrontBffException(HttpStatus.NOT_FOUND, message);
    }

    public static FrontBffException badRequest(String message) {
        return new FrontBffException(HttpStatus.BAD_REQUEST, message);
    }

    public static FrontBffException internalError(String message) {
        return new FrontBffException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static FrontBffException internalError(String message, Throwable cause) {
        return new FrontBffException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

