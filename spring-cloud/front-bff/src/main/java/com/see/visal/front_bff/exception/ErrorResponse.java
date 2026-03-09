package com.see.visal.front_bff.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

/**
 * Uniform error response body returned by the Front BFF service.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        ZonedDateTime timestamp
) {}

