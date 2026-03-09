package com.see.visal.itp_gateway_server.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception for the Gateway Server.
 */
public class GatewayException extends RuntimeException {

    private final HttpStatus status;

    public GatewayException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public GatewayException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // ── Convenience factories ─────────────────────────────────────────────────

    public static GatewayException notFound(String message) {
        return new GatewayException(HttpStatus.NOT_FOUND, message);
    }

    public static GatewayException badRequest(String message) {
        return new GatewayException(HttpStatus.BAD_REQUEST, message);
    }

    public static GatewayException serviceUnavailable(String message) {
        return new GatewayException(HttpStatus.SERVICE_UNAVAILABLE, message);
    }

    public static GatewayException internalError(String message) {
        return new GatewayException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static GatewayException internalError(String message, Throwable cause) {
        return new GatewayException(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

