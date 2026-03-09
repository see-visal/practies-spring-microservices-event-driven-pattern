package com.see.visal.admin_e_backing.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * Global exception handler for the Admin BFF service (WebFlux / Gateway).
 * Registered with high precedence so it runs before the default error handler.
 */
@Component
@Order(-2)
public class AdminBffExceptionHandler implements WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AdminBffExceptionHandler.class);

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status;
        String message;

        if (ex instanceof AdminBffException adminEx) {
            log.error("AdminBffException [{}]: {}", adminEx.getStatus(), adminEx.getMessage());
            status  = adminEx.getStatus();
            message = adminEx.getMessage();

        } else if (ex instanceof ResponseStatusException responseEx) {
            log.error("ResponseStatusException [{}]: {}", responseEx.getStatusCode(), responseEx.getReason());
            status  = HttpStatus.resolve(responseEx.getStatusCode().value());
            if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = responseEx.getReason() != null ? responseEx.getReason() : responseEx.getMessage();

        } else {
            log.error("Unhandled admin-bff exception: {}", ex.getMessage(), ex);
            status  = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An unexpected error occurred";
        }

        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value(),
                ZonedDateTime.now()
        );

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = "{\"status\":500,\"error\":\"Internal Server Error\"}".getBytes();
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

