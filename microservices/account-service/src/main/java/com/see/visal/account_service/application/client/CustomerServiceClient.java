package com.see.visal.account_service.application.client;

import static com.see.visal.account_service.application.interceptor.CustomerValidationInterceptor.CustomerNotFoundException;
import static com.see.visal.account_service.application.interceptor.CustomerValidationInterceptor.CustomerServiceUnavailableException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {

    private final WebClient.Builder webClientBuilder;

    public void validateCustomerExists(UUID customerId) {
        try {
            log.info("Validating customer {} in customer-service", customerId);

            webClientBuilder.build()
                    .get()
                    .uri("http://customer/api/customers/{customerId}/history", customerId)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            clientResponse -> Mono.error(
                                    new CustomerNotFoundException(
                                            "Customer not found with ID: " + customerId))
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> Mono.error(
                                    new CustomerServiceUnavailableException(
                                            "Customer service is unavailable. Please try again later."))
                    )
                    .bodyToMono(Void.class)
                    .block();

            log.info("Customer {} validated successfully", customerId);

        } catch (CustomerNotFoundException | CustomerServiceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error validating customer {}: {}", customerId, e.getMessage());
            throw new CustomerServiceUnavailableException(
                    "Unable to reach customer-service. Please ensure it is running.");
        }
    }
}

