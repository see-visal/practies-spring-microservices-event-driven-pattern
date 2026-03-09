package com.see.visal.itp_gateway_server.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
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
 * Global exception handler for the Gateway Server (WebFlux).
 * Registered with high precedence so it runs before the default error handler.
 */
@Component
@Order(-2)
@Slf4j
public class GatewayExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status;
        String message;

        if (ex instanceof GatewayException gatewayEx) {
            log.error("GatewayException [{}]: {}", gatewayEx.getStatus(), gatewayEx.getMessage());
            status  = gatewayEx.getStatus();
            message = gatewayEx.getMessage();

        } else if (ex instanceof ResponseStatusException responseEx) {
            log.error("ResponseStatusException [{}]: {}", responseEx.getStatusCode(), responseEx.getReason());
            status  = HttpStatus.resolve(responseEx.getStatusCode().value());
            if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = responseEx.getReason() != null ? responseEx.getReason() : responseEx.getMessage();

        } else {
            log.error("Unhandled gateway exception: {}", ex.getMessage(), ex);
            status  = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An unexpected error occurred in the gateway";
        }

        String path = exchange.getRequest().getPath().value();

        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                ZonedDateTime.now()
        );

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = ("{\"status\":500,\"error\":\"Internal Server Error\"}").getBytes();
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

