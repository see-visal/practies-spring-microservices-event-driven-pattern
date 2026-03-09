package com.see.visal.account_service.application.interceptor;

import com.see.visal.account_service.application.client.CustomerServiceClient;
import com.see.visal.account_service.domain.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerValidationInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final CustomerServiceClient customerServiceClient;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, message) -> {
            if (message.getPayload() instanceof CreateAccountCommand cmd) {
                log.info("Validating customer {} before creating account", cmd.customerId().getValue());
                customerServiceClient.validateCustomerExists(cmd.customerId().getValue());
            }
            return message;
        };
    }

    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }

    public static class CustomerServiceUnavailableException extends RuntimeException {
        public CustomerServiceUnavailableException(String message) {
            super(message);
        }
    }
}

