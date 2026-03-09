package com.see.visal.account_service.application.interceptor;

import com.see.visal.account_service.data.entity.BranchEntity;
import com.see.visal.account_service.data.repository.BranchRepository;
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
public class BranchValidationInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final BranchRepository branchRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, message) -> {
            if (message.getPayload() instanceof CreateAccountCommand cmd) {
                log.info("Intercepting CreateAccountCommand for branch validation");
                validateBranchForAccountCreation(cmd);
            }
            return message;
        };
    }

    private void validateBranchForAccountCreation(CreateAccountCommand command) {
        log.info("Validating branch {} for account creation", command.branchId().getValue());

        BranchEntity branch = branchRepository.findById(command.branchId().getValue())
                .orElseThrow(() -> {
                    log.error("Branch not found: {}", command.branchId().getValue());
                    return new BranchNotFoundException(
                            "Branch with ID " + command.branchId().getValue() + " does not exist. " +
                            "Please select a valid branch."
                    );
                });

        if (branch.getIsOpening() == null || !branch.getIsOpening()) {
            log.error("Branch {} is not open for business", command.branchId().getValue());
            throw new BranchNotActiveException(
                    "Branch '" + branch.getBranchName() + "' is currently not open for new account creation. " +
                    "Please select an active branch."
            );
        }

        log.info("Branch validation successful for branch: {}", branch.getBranchName());
    }

    public static class BranchNotFoundException extends RuntimeException {
        public BranchNotFoundException(String message) {
            super(message);
        }
    }

    public static class BranchNotActiveException extends RuntimeException {
        public BranchNotActiveException(String message) {
            super(message);
        }
    }
}

