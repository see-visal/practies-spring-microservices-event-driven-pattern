package com.see.visal.account_service.application.interceptor;

import com.see.visal.account_service.data.entity.AccountTypeEntity;
import com.see.visal.account_service.data.repository.AccountTypeRepository;
import com.see.visal.account_service.domain.command.CreateAccountCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountTypeValidationInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final AccountTypeRepository accountTypeRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, message) -> {
            if (message.getPayload() instanceof CreateAccountCommand cmd) {
                log.info("Intercepting CreateAccountCommand for account type validation");
                validateAccountTypeAndBalance(cmd);
            }
            return message;
        };
    }

    private void validateAccountTypeAndBalance(CreateAccountCommand command) {
        log.info("Validating account type {} for account creation", command.accountTypeCode());

        AccountTypeEntity accountType = accountTypeRepository.findByAccountTypeCode(command.accountTypeCode())
                .orElseThrow(() -> {
                    log.error("Account type not found: {}", command.accountTypeCode());
                    return new AccountTypeNotFoundException(
                            "Account type " + command.accountTypeCode() + " is not available. " +
                            "Please select a valid account type."
                    );
                });

        if (accountType.getIsActive() == null || !accountType.getIsActive()) {
            log.error("Account type {} is not active", command.accountTypeCode());
            throw new AccountTypeNotActiveException(
                    "Account type '" + accountType.getAccountTypeName() + "' is currently not available for new accounts."
            );
        }

        BigDecimal initialBalance = command.initialBalance().amount();
        BigDecimal minimumBalance = accountType.getMinimumBalance();

        if (minimumBalance != null && initialBalance.compareTo(minimumBalance) < 0) {
            log.error("Initial balance {} is less than minimum {} for account type {}",
                    initialBalance, minimumBalance, command.accountTypeCode());
            throw new InsufficientInitialBalanceException(
                    String.format(
                            "Initial balance of %s is below the minimum required balance of %s for %s account. " +
                            "Please deposit at least %s to open this account.",
                            initialBalance, minimumBalance, accountType.getAccountTypeName(), minimumBalance
                    )
            );
        }

        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Initial balance cannot be negative: {}", initialBalance);
            throw new InvalidInitialBalanceException(
                    "Initial balance cannot be negative. Please provide a valid positive amount."
            );
        }

        log.info("Account type validation successful: {} with initial balance {}",
                accountType.getAccountTypeName(), initialBalance);
    }

    public static class AccountTypeNotFoundException extends RuntimeException {
        public AccountTypeNotFoundException(String message) {
            super(message);
        }
    }

    public static class AccountTypeNotActiveException extends RuntimeException {
        public AccountTypeNotActiveException(String message) {
            super(message);
        }
    }

    public static class InsufficientInitialBalanceException extends RuntimeException {
        public InsufficientInitialBalanceException(String message) {
            super(message);
        }
    }

    public static class InvalidInitialBalanceException extends RuntimeException {
        public InvalidInitialBalanceException(String message) {
            super(message);
        }
    }
}

